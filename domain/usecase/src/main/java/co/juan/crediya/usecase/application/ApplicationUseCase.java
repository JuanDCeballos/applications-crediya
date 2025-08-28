package co.juan.crediya.usecase.application;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.application.gateways.ApplicationRepository;
import co.juan.crediya.model.dto.LoanApplicationDTO;
import co.juan.crediya.model.exceptions.CrediYaException;
import co.juan.crediya.model.exceptions.ErrorCode;
import co.juan.crediya.model.user.UserGateway;
import co.juan.crediya.usecase.loantype.LoanTypeUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplicationUseCase {

    private final ApplicationRepository applicationRepository;
    private final LoanTypeUseCase loanTypeUseCase;
    private final UserGateway userGateway;

    public Mono<Application> saveApplication(LoanApplicationDTO loanApplicationDTO) {

        Mono<String> userEmailByDniMono = userGateway.getUserEmailByDni(loanApplicationDTO.getDni());
        Mono<Boolean> loanTypeMono = loanTypeUseCase.getLoanTypeById(loanApplicationDTO.getIdLoanType()).hasElement();

        return Mono.zip(userEmailByDniMono, loanTypeMono)
                .flatMap(result -> {
                    String userEmail = result.getT1();
                    boolean loanType = result.getT2();

                    if (userEmail.isBlank()) {
                        return Mono.error(new CrediYaException(ErrorCode.USER_NOT_FOUND));
                    }

                    if (!loanType) {
                        return Mono.error(new CrediYaException(ErrorCode.INVALID_LOAN_TYPE));
                    }

                    Application application = Application.builder()
                            .email(userEmail)
                            .term(loanApplicationDTO.getTerm())
                            .amount(loanApplicationDTO.getAmount())
                            .idLoanType(loanApplicationDTO.getIdLoanType())
                            .idState(1L).build();

                    return applicationRepository.saveApplication(application);
                });

    }

    public Flux<Application> getAllApplications() {
        return applicationRepository.findAllApplications();
    }
}
