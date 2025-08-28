package co.juan.crediya.r2dbc;

import co.juan.crediya.model.loantype.LoanType;
import co.juan.crediya.r2dbc.entity.LoanTypeEntity;
import co.juan.crediya.r2dbc.repository.LoanTypeReactiveRepository;
import co.juan.crediya.r2dbc.repository.adapter.LoanTypeRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanTypeRepositoryAdapterTest {

    @InjectMocks
    LoanTypeRepositoryAdapter loanTypeRepositoryAdapter;

    @Mock
    LoanTypeReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private LoanTypeEntity loanTypeEntity;
    private LoanType loanType;

    @BeforeEach
    void initMocks() {
        loanTypeEntity = new LoanTypeEntity();
        loanTypeEntity.setId(1L);
        loanTypeEntity.setName("Libre inversion");
        loanTypeEntity.setInterestRate(new BigDecimal("2"));
        loanTypeEntity.setMinAmount(new BigDecimal("500000"));
        loanTypeEntity.setMaxAmount(new BigDecimal("15000000"));
        loanTypeEntity.setAutomaticValidation(true);

        loanType = new LoanType();
        loanType.setIdLoanType(1L);
        loanType.setName("Libre inversion");
        loanType.setInterestRate(new BigDecimal("2"));
        loanType.setMinAmount(new BigDecimal("500000"));
        loanType.setMaxAmount(new BigDecimal("15000000"));
        loanType.setAutomaticValidation(true);
    }

    @Test
    void mustFindValueById() {

        when(repository.findById(1L)).thenReturn(Mono.just(loanTypeEntity));
        when(mapper.map(loanTypeEntity, LoanType.class)).thenReturn(loanType);

        Mono<LoanType> result = loanTypeRepositoryAdapter.findByIdLoanType(1L);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(loanType))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        when(repository.findAll()).thenReturn(Flux.just(loanTypeEntity));
        when(mapper.map(loanTypeEntity, LoanType.class)).thenReturn(loanType);

        Flux<LoanType> result = loanTypeRepositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(loanType))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(mapper.map(any(LoanType.class), eq(LoanTypeEntity.class))).thenReturn(loanTypeEntity);
        when(repository.save(any(LoanTypeEntity.class))).thenReturn(Mono.just(loanTypeEntity));
        when(mapper.map(loanTypeEntity, LoanType.class)).thenReturn(loanType);

        Mono<LoanType> result = loanTypeRepositoryAdapter.save(loanType);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(loanType))
                .verifyComplete();
    }
}
