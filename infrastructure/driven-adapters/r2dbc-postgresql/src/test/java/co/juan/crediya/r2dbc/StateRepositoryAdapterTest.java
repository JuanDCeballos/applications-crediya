package co.juan.crediya.r2dbc;

import co.juan.crediya.model.states.States;
import co.juan.crediya.r2dbc.entity.StateEntity;
import co.juan.crediya.r2dbc.repository.StateReactiveRepository;
import co.juan.crediya.r2dbc.repository.adapter.StateRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StateRepositoryAdapterTest {

    @InjectMocks
    StateRepositoryAdapter stateRepositoryAdapter;

    @Mock
    StateReactiveRepository statesRepository;

    @Mock
    ObjectMapper mapper;

    private StateEntity stateEntity;
    private States states;

    @BeforeEach
    void initMocks() {
        stateEntity = new StateEntity();
        stateEntity.setId(1L);
        stateEntity.setName("Pendiente de revisión");
        stateEntity.setDescription("En proceso de revisión por asesor");

        states = new States();
        states.setIdState(1L);
        states.setName("Pendiente de revisión");
        states.setDescription("En proceso de revisión por asesor");
    }

    @Test
    void mustFindValueById() {
        when(statesRepository.findById(1L)).thenReturn(Mono.just(stateEntity));
        when(mapper.map(stateEntity, States.class)).thenReturn(states);

        Mono<States> result = stateRepositoryAdapter.findByIdState(1L);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(states))
                .verifyComplete();
    }
}
