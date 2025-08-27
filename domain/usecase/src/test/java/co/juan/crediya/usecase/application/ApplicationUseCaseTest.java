package co.juan.crediya.usecase.application;


import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.application.gateways.ApplicationRepository;
import co.juan.crediya.model.exceptions.CrediYaException;
import co.juan.crediya.model.exceptions.ErrorCode;
import co.juan.crediya.model.loantype.LoanType;
import co.juan.crediya.usecase.loantype.LoanTypeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUseCaseTest {

    @InjectMocks
    private ApplicationUseCase applicationUseCase;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private LoanTypeUseCase loanTypeUseCase;

    private LoanType loanType;
    private Application application;

    @BeforeEach
    void initMocks() {
        application = new Application();
        application.setIdApplication(1L);
        application.setAmount(BigDecimal.TEN);
        application.setTerm(12);
        application.setEmail("myEmail@email.com");
        application.setIdState(1L);
        application.setIdLoanType(1L);

        loanType = new LoanType(
                1L,
                "Libre Inversion",
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.ONE,
                true
        );
    }

    @Test
    void saveApplication_shouldSave() {
        loanType = new LoanType(
                1L,
                "Libre Inversion",
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.ONE,
                true
        );

        when(loanTypeUseCase.getLoanTypeById(anyLong())).thenReturn(Mono.just(loanType));
        when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(application));

        Mono<Application> response = applicationUseCase.saveApplication(application);

        StepVerifier.create(response)
                .expectNextMatches(value -> value.equals(application))
                .verifyComplete();

        verify(loanTypeUseCase, times(1)).getLoanTypeById(anyLong());
        verify(applicationRepository, times(1)).saveApplication(any(Application.class));
    }

    @Test
    void saveApplication_shouldReturnException() {
        application.setIdLoanType(2L);

        when(loanTypeUseCase.getLoanTypeById(anyLong())).thenThrow(new CrediYaException(ErrorCode.INVALID_LOAN_TYPE));

        Executable executable = () -> applicationUseCase.saveApplication(application);

        CrediYaException exception = assertThrows(CrediYaException.class, executable);
        assertEquals("There's not an application with that id", exception.getMessage());

        verify(loanTypeUseCase, times(1)).getLoanTypeById(anyLong());
        verify(applicationRepository, times(0)).saveApplication(any(Application.class));
    }

    @Test
    void getAllApplications_shouldReturnSomething() {
        when(applicationRepository.findAllApplications()).thenReturn(Flux.just(application));

        Flux<Application> response = applicationUseCase.getAllApplications();

        StepVerifier.create(response)
                .expectNextMatches(value -> value.equals(application))
                .verifyComplete();

        verify(applicationRepository, times(1)).findAllApplications();
    }

}