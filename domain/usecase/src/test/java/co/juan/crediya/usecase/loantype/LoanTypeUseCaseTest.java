package co.juan.crediya.usecase.loantype;

import co.juan.crediya.model.loantype.LoanType;
import co.juan.crediya.model.loantype.gateways.LoanTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanTypeUseCaseTest {

    @InjectMocks
    private LoanTypeUseCase loanTypeUseCase;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    private LoanType loanType;

    @Test
    void getLoanTypeById_shouldReturnSomething() {
        loanType = new LoanType(
                1L,
                "Libre Inversion",
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.ONE,
                true
        );

        when(loanTypeRepository.findByIdLoanType(anyLong())).thenReturn(Mono.just(loanType));

        Mono<LoanType> response = loanTypeUseCase.getLoanTypeById(1L);

        StepVerifier.create(response)
                .expectNextMatches(value -> value.equals(loanType))
                .verifyComplete();

        verify(loanTypeRepository, times(1)).findByIdLoanType(anyLong());
    }


}
