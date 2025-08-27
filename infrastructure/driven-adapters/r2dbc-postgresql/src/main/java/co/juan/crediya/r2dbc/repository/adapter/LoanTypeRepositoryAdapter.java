package co.juan.crediya.r2dbc.repository.adapter;

import co.juan.crediya.model.loantype.LoanType;
import co.juan.crediya.model.loantype.gateways.LoanTypeRepository;
import co.juan.crediya.r2dbc.entity.LoanTypeEntity;
import co.juan.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.juan.crediya.r2dbc.repository.LoanTypeReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoanTypeRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType,
        LoanTypeEntity,
        Long,
        LoanTypeReactiveRepository
        > implements LoanTypeRepository {
    public LoanTypeRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanType.class));
    }

    @Override
    public Mono<LoanType> findByIdLoanType(Long id) {
        return findById(id);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return repository.existsById(id);
    }
}
