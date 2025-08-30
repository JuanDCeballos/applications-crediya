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
import reactor.util.function.Tuple2;

@RequiredArgsConstructor
public class ApplicationUseCase {

    private final ApplicationRepository applicationRepository;
    private final LoanTypeUseCase loanTypeUseCase;
    private final UserGateway userGateway;

    public Mono<Application> saveApplication(LoanApplicationDTO loanApplicationDTO) {

        Mono<String> userEmailByDniMono = userGateway.getUserEmailByDni(loanApplicationDTO.getDni());
        Mono<Boolean> loanTypeMono = loanTypeUseCase.getLoanTypeById(loanApplicationDTO.getIdLoanType()).hasElement();

        return Mono.zip(userEmailByDniMono, loanTypeMono)
                .filter(tuple -> !tuple.getT1().isBlank())
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.USER_NOT_FOUND)))
                .filter(Tuple2::getT2)
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.INVALID_LOAN_TYPE)))
                .flatMap(tuple ->
                        applicationRepository.saveApplication(Application.builder()
                                .email(tuple.getT1())
                                .term(loanApplicationDTO.getTerm())
                                .amount(loanApplicationDTO.getAmount())
                                .idLoanType(loanApplicationDTO.getIdLoanType())
                                .idState(1L).build())
                );
    }

    public Flux<Application> getAllApplications() {
        return applicationRepository.findAllApplications();
    }
}
