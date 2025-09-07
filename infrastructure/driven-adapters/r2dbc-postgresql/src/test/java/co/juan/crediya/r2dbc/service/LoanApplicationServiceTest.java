package co.juan.crediya.r2dbc.service;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.dto.FilteredApplicationDto;
import co.juan.crediya.model.dto.LoanApplicationDTO;
import co.juan.crediya.security.JwtProvider;
import co.juan.crediya.security.SecurityContextRepository;
import co.juan.crediya.usecase.application.ApplicationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.transaction.reactive.TransactionCallback;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @InjectMocks
    LoanApplicationService loanApplicationService;

    @Mock
    TransactionalOperator transactionalOperator;

    @Mock
    ApplicationUseCase applicationUseCase;

    @Mock
    SecurityContextRepository securityContextRepository;

    @Mock
    JwtProvider jwtProvider;

    private Application application;
    private LoanApplicationDTO loanApplicationDTO;
    private final Long allRows = 21L;
    private final FilteredApplicationDto filteredApplicationDto =
            new FilteredApplicationDto(new BigDecimal("1000"), 12,
                    "juan.juan@gmail.com", "Pedro",
                    "Libre inversion", new BigDecimal(2),
                    "Pendiente de revision", new BigDecimal(10000),
                    new BigDecimal(100));
    private final long status = 1L;

    @BeforeEach
    void initMocks() {
        application = new Application();
        application.setIdApplication(1L);
        application.setTerm(1);
        application.setAmount(new BigDecimal("150000"));
        application.setIdState(1L);
        application.setIdLoanType(1L);
        application.setEmail("myEmail@main.com");

        loanApplicationDTO = new LoanApplicationDTO();
        loanApplicationDTO.setDni("12345");
        loanApplicationDTO.setIdLoanType(1L);
        loanApplicationDTO.setTerm(12);
        loanApplicationDTO.setAmount(new BigDecimal("4500000"));
        loanApplicationDTO.setEmailLogged("myEmail@main.com");
    }

    @Test
    void createApplication_shouldSaveUserSuccessfully() {
        when(applicationUseCase.saveApplication(any(LoanApplicationDTO.class))).thenReturn(Mono.just(application));

        when(transactionalOperator.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback = invocation.getArgument(0);
                    return ((Mono<Application>) callback.doInTransaction(null)).flux();
                });

        Mono<Application> result = loanApplicationService.createApplication(loanApplicationDTO);

        StepVerifier.create(result)
                .expectNext(application)
                .verifyComplete();

        verify(applicationUseCase).saveApplication(loanApplicationDTO);
        verify(transactionalOperator).execute(any());
    }

    @Test
    void createApplication_shouldHandleError() {
        RuntimeException exception = new RuntimeException("Error al guardar");

        when(applicationUseCase.saveApplication(any(LoanApplicationDTO.class))).thenReturn(Mono.error(exception));

        when(transactionalOperator.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback = invocation.getArgument(0);
                    return ((Mono<Application>) callback.doInTransaction(null)).flux();
                });

        Mono<Application> result = loanApplicationService.createApplication(loanApplicationDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error al guardar"))
                .verify();

        verify(applicationUseCase).saveApplication(loanApplicationDTO);
        verify(transactionalOperator).execute(any());
    }

    @Test
    void getAllApplicationsPaging() {
        List<FilteredApplicationDto> applications = List.of(filteredApplicationDto);

        when(applicationUseCase.countAll(anyLong())).thenReturn(Mono.just(allRows));
        when(applicationUseCase.getAllApplicationsPaging(anyLong(), anyLong(), anyInt())).thenReturn(Mono.just(applications));

        long offset = 0L;
        int limit = 5;
        Mono<Page<FilteredApplicationDto>> result = loanApplicationService.getAllApplicationsPaging(offset, limit, status);

        StepVerifier.create(result)
                .assertNext(page -> {
                    assertThat(page.getTotalElements()).isEqualTo(21L);
                    assertThat(page.getContent()).hasSize(1);
                })
                .verifyComplete();
    }

    @Test
    void countAll() {
        when(applicationUseCase.countAll(anyLong())).thenReturn(Mono.just(allRows));

        Mono<Long> result = loanApplicationService.countAll(status);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(allRows))
                .verifyComplete();
    }
}
