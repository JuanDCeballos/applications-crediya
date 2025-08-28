package co.juan.crediya.r2dbc;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.r2dbc.entity.ApplicationEntity;
import co.juan.crediya.r2dbc.repository.ApplicationReactiveRepository;
import co.juan.crediya.r2dbc.repository.adapter.ApplicationRepositoryAdapter;
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
class ApplicationRepositoryAdapterTest {

    @InjectMocks
    ApplicationRepositoryAdapter repositoryAdapter;

    @Mock
    ApplicationReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private ApplicationEntity applicationEntity;
    private Application application;

    @BeforeEach
    void initMocks() {
        applicationEntity = new ApplicationEntity();
        applicationEntity.setIdApplication(1L);
        applicationEntity.setAmount(BigDecimal.TEN);
        applicationEntity.setTerm(12);
        applicationEntity.setEmail("myEmail@email.com");
        applicationEntity.setIdState(1L);
        applicationEntity.setIdLoanType(1L);

        application = new Application();
        application.setIdApplication(1L);
        application.setAmount(BigDecimal.TEN);
        application.setTerm(12);
        application.setEmail("myEmail@email.com");
        application.setIdState(1L);
        application.setIdLoanType(1L);
    }

    @Test
    void mustFindValueById() {

        when(repository.findById(1L)).thenReturn(Mono.just(applicationEntity));
        when(mapper.map(applicationEntity, Application.class)).thenReturn(application);

        Mono<Application> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(application))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        when(repository.findAll()).thenReturn(Flux.just(applicationEntity));
        when(mapper.map(applicationEntity, Application.class)).thenReturn(application);

        Flux<Application> result = repositoryAdapter.findAllApplications();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(application))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(mapper.map(any(Application.class), eq(ApplicationEntity.class))).thenReturn(applicationEntity);
        when(repository.save(any(ApplicationEntity.class))).thenReturn(Mono.just(applicationEntity));
        when(mapper.map(applicationEntity, Application.class)).thenReturn(application);

        Mono<Application> result = repositoryAdapter.saveApplication(application);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(application))
                .verifyComplete();
    }
}
