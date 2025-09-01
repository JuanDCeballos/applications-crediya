package co.juan.crediya.r2dbc.repository.adapter;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.application.gateways.ApplicationRepository;
import co.juan.crediya.r2dbc.entity.ApplicationEntity;
import co.juan.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.juan.crediya.r2dbc.repository.ApplicationReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class ApplicationRepositoryAdapter extends ReactiveAdapterOperations<
        Application,
        ApplicationEntity,
        Long,
        ApplicationReactiveRepository
        > implements ApplicationRepository {
    public ApplicationRepositoryAdapter(ApplicationReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Application.class));
    }

    @Override
    public Mono<Application> saveApplication(Application application) {
        return save(application);
    }

    @Override
    public Mono<List<Application>> findAllApplications(long offset, int limit) {
        return repository.findAllByPage(offset, limit).collectList();
    }

    @Override
    public Mono<Long> countAll() {
        return repository.count();
    }
}
