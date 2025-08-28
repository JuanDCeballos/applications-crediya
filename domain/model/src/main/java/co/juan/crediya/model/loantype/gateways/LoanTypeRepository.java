package co.juan.crediya.model.loantype.gateways;

import co.juan.crediya.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {

    Mono<LoanType> findByIdLoanType(Long id);
}
