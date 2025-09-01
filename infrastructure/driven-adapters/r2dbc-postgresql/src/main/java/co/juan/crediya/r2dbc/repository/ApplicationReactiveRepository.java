package co.juan.crediya.r2dbc.repository;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.r2dbc.entity.ApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, Long>, ReactiveQueryByExampleExecutor<ApplicationEntity> {

    @Query("SELECT * FROM applications OFFSET :offset LIMIT :limit")
    Flux<Application> findAllByPage(long offset, int limit);

    @Query("SELECT COUNT(*) FROM applications")
    Mono<Long> countAll();
}
