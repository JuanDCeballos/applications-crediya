package co.juan.crediya.r2dbc.repository.adapter;

import co.juan.crediya.model.states.States;
import co.juan.crediya.model.states.gateways.StatesRepository;
import co.juan.crediya.r2dbc.entity.StateEntity;
import co.juan.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.juan.crediya.r2dbc.repository.StateReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class StateRepositoryAdapter extends ReactiveAdapterOperations<
        States,
        StateEntity,
        Long,
        StateReactiveRepository
        > implements StatesRepository {
    public StateRepositoryAdapter(StateReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, States.class));
    }

    @Override
    public Mono<States> findByIdState(Long id) {
        return findById(id);
    }
}
