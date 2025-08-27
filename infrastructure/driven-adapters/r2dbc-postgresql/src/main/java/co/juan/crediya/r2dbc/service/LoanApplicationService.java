package co.juan.crediya.r2dbc.service;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.usecase.application.ApplicationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanApplicationService {

    private final ApplicationUseCase applicationUseCase;
    private final TransactionalOperator transactionalOperator;

    public Mono<Application> createApplication(Application application) {
        return transactionalOperator.execute(transaction ->
                        applicationUseCase.saveApplication(application)
                )
                .doOnNext(applicationSaved -> log.info("Application of {} saved successfully.", applicationSaved.getEmail()))
                .doOnError(throwable -> log.error("Error while trying to save the application: {}", throwable.getMessage()))
                .single();
    }
}
