package co.juan.crediya.usecase.states;

import co.juan.crediya.model.states.States;
import co.juan.crediya.model.states.gateways.StatesRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StatesUseCase {

    private final StatesRepository statesRepository;

    public Mono<States> getStatusById(Long id) {
        return statesRepository.findByIdState(id);
    }
}
