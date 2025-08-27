package co.juan.crediya.r2dbc.service;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.usecase.application.ApplicationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionCallback;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @InjectMocks
    LoanApplicationService loanApplicationService;

    @Mock
    TransactionalOperator transactionalOperator;

    @Mock
    ApplicationUseCase applicationUseCase;

    private Application application;

    @BeforeEach
    void initMocks() {
        application = new Application();
        application.setIdApplication(1L);
        application.setTerm(1);
        application.setAmount(new BigDecimal("150000"));
        application.setIdState(1L);
        application.setIdLoanType(1L);
    }

    @Test
    void createApplication_shouldSaveUserSuccessfully() {
        when(applicationUseCase.saveApplication(any(Application.class))).thenReturn(Mono.just(application));

        when(transactionalOperator.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback = invocation.getArgument(0);
                    return ((Mono<Application>) callback.doInTransaction(null)).flux();
                });

        Mono<Application> result = loanApplicationService.createApplication(application);

        StepVerifier.create(result)
                .expectNext(application)
                .verifyComplete();

        verify(applicationUseCase).saveApplication(application);
        verify(transactionalOperator).execute(any());
    }

    @Test
    void createApplication_shouldHandleError() {
        RuntimeException exception = new RuntimeException("Error al guardar");

        when(applicationUseCase.saveApplication(any(Application.class))).thenReturn(Mono.error(exception));

        when(transactionalOperator.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback = invocation.getArgument(0);
                    return ((Mono<Application>) callback.doInTransaction(null)).flux();
                });

        Mono<Application> result = loanApplicationService.createApplication(application);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error al guardar"))
                .verify();

        verify(applicationUseCase).saveApplication(application);
        verify(transactionalOperator).execute(any());
    }
}
