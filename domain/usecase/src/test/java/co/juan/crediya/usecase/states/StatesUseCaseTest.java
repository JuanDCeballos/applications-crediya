package co.juan.crediya.usecase.states;

import co.juan.crediya.model.states.States;
import co.juan.crediya.model.states.gateways.StatesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatesUseCaseTest {

    @InjectMocks
    private StatesUseCase statesUseCase;

    @Mock
    private StatesRepository statesRepository;

    private States states;

    @Test
    void getStatusById_shouldReturnSomething() {
        states = new States();
        states.setIdState(1L);
        states.setName("Pendiente de revisión");
        states.setDescription("Solicitud pendiente de revisión");

        when(statesRepository.findByIdState(anyLong())).thenReturn(Mono.just(states));

        Mono<States> response = statesUseCase.getStatusById(1L);

        StepVerifier.create(response)
                .expectNextMatches(value -> value.equals(states))
                .verifyComplete();

        verify(statesRepository, times(1)).findByIdState(anyLong());
    }
}
