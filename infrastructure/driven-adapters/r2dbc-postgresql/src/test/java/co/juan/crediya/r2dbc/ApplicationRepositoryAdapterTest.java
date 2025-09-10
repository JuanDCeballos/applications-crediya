package co.juan.crediya.r2dbc;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.dto.FilteredApplicationDto;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
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
    private final FilteredApplicationDto filteredApplicationDto =
            new FilteredApplicationDto(1L, new BigDecimal("1000"), 12,
                    "juan.juan@gmail.com", "Pedro",
                    "Libre inversion", new BigDecimal(2),
                    "Pendiente de revision", new BigDecimal(10000),
                    new BigDecimal(100));
    long status = 1L;

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
        when(repository.findAllByPage(anyLong(), anyLong(), anyInt())).thenReturn(Flux.just(filteredApplicationDto));

        long offset = 0L;
        int limit = 5;
        Mono<List<FilteredApplicationDto>> result = repositoryAdapter.findAllApplicationsPaging(status, offset, limit);

        StepVerifier.create(result)
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    FilteredApplicationDto dto = list.get(0);
                    assertThat(dto.name()).isEqualTo("Pedro");
                    assertThat(dto.baseSalary()).isEqualTo(new BigDecimal(10000));
                    assertThat(dto.monthlyRequestAmount()).isEqualTo(new BigDecimal("100"));
                })
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

    @Test
    void mustCountAllValues() {
        Long allRows = 21L;
        when(repository.countAll(anyLong())).thenReturn(Mono.just(allRows));

        Mono<Long> result = repositoryAdapter.countAll(status);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(allRows))
                .verifyComplete();
    }
}
