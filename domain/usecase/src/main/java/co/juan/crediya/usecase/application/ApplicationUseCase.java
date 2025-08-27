package co.juan.crediya.usecase.application;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.application.gateways.ApplicationRepository;
import co.juan.crediya.model.exceptions.CrediYaException;
import co.juan.crediya.model.exceptions.ErrorCode;
import co.juan.crediya.model.loantype.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplicationUseCase {

    private final ApplicationRepository applicationRepository;
    private final LoanTypeRepository loanTypeRepository;

    public Mono<Application> saveApplication(Application application) {
        return loanTypeRepository.existsById(application.getIdLoanType())
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        return Mono.error(new CrediYaException(ErrorCode.INVALID_LOAN_TYPE));
                    }
                    application.setIdState(1L);
                    return applicationRepository.saveApplication(application);
                });
    }

    public Flux<Application> getAllApplications() {
        return applicationRepository.findAllApplications();
    }
}
