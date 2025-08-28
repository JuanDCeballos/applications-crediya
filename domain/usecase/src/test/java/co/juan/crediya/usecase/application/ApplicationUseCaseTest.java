package co.juan.crediya.usecase.application;


import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.application.gateways.ApplicationRepository;
import co.juan.crediya.model.dto.LoanApplicationDTO;
import co.juan.crediya.model.exceptions.CrediYaException;
import co.juan.crediya.model.exceptions.ErrorCode;
import co.juan.crediya.model.loantype.LoanType;
import co.juan.crediya.model.user.UserGateway;
import co.juan.crediya.usecase.loantype.LoanTypeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

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

    @Mock
    private UserGateway userGateway;

    private LoanType loanType;
    private Application application;
    private LoanApplicationDTO loanApplicationDTO;

    private String userEmail = "myEmail@mail.com";

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

        loanApplicationDTO = new LoanApplicationDTO();
        loanApplicationDTO.setDni("12345");
        loanApplicationDTO.setIdLoanType(1L);
        loanApplicationDTO.setTerm(12);
        loanApplicationDTO.setAmount(BigDecimal.TEN);
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
        when(userGateway.getUserEmailByDni(anyString())).thenReturn(Mono.just(userEmail));

        Mono<Application> response = applicationUseCase.saveApplication(loanApplicationDTO);

        StepVerifier.create(response)
                .expectNextMatches(value -> value.equals(application))
                .verifyComplete();

        verify(loanTypeUseCase, times(1)).getLoanTypeById(anyLong());
        verify(applicationRepository, times(1)).saveApplication(any(Application.class));
        verify(userGateway, times(1)).getUserEmailByDni(anyString());
    }

    @Test
    void saveApplication_shouldThrowWhenUserNotFound() {
        when(userGateway.getUserEmailByDni(anyString())).thenReturn(Mono.just(""));
        when(loanTypeUseCase.getLoanTypeById(anyLong())).thenReturn(Mono.just(loanType));

        Mono<Application> response = applicationUseCase.saveApplication(loanApplicationDTO);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof CrediYaException &&
                        ((CrediYaException) throwable).getErrorCode() == ErrorCode.USER_NOT_FOUND)
                .verify();

        verify(userGateway, times(1)).getUserEmailByDni(anyString());
        verify(applicationRepository, never()).saveApplication(any());
    }

    @Test
    void saveApplication_shouldThrowWhenInvalidLoanType() {
        when(userGateway.getUserEmailByDni(anyString())).thenReturn(Mono.just(userEmail));
        when(loanTypeUseCase.getLoanTypeById(anyLong())).thenReturn(Mono.empty());

        Mono<Application> response = applicationUseCase.saveApplication(loanApplicationDTO);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof CrediYaException &&
                        ((CrediYaException) throwable).getErrorCode() == ErrorCode.INVALID_LOAN_TYPE)
                .verify();

        verify(loanTypeUseCase, times(1)).getLoanTypeById(anyLong());
        verify(applicationRepository, never()).saveApplication(any());
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