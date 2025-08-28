package co.juan.crediya.model.states.gateways;

import co.juan.crediya.model.states.States;
import reactor.core.publisher.Mono;

public interface StatesRepository {

    Mono<States> findByIdState(Long id);
}
