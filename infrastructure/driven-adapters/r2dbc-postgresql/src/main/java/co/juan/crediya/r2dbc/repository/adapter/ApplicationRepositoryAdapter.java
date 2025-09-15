package co.juan.crediya.r2dbc.repository.adapter;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.dto.FilteredApplicationDto;
import co.juan.crediya.model.application.gateways.ApplicationRepository;
import co.juan.crediya.model.dto.UpdateLoanApplicationRequestDto;
import co.juan.crediya.r2dbc.entity.ApplicationEntity;
import co.juan.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.juan.crediya.r2dbc.repository.ApplicationReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
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
    public Mono<List<FilteredApplicationDto>> findAllApplicationsPaging(long status, long offset, int limit) {
        return repository.findAllByPage(status, offset, limit).collectList();
    }

    @Override
    public Mono<Long> countAll(long status) {
        return repository.countAll(status);
    }

    @Override
    public Mono<Application> findApplicationById(Long idApplication) {
        return findById(idApplication);
    }

    @Override
    public Mono<Application> updateLoanApplication(UpdateLoanApplicationRequestDto updateLoanApplicationRequestDto) {
        return repository.updateStatusApplication(updateLoanApplicationRequestDto.getIdState(), updateLoanApplicationRequestDto.getIdApplication());
    }

    @Override
    public Flux<FilteredApplicationDto> getApplicationsByUserEmailAndState(String email, Long idState) {
        return repository.getApplicationsByUserEmailAndState(email, idState);
    }
}
