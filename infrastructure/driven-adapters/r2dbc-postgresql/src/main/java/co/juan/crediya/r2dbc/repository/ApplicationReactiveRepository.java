package co.juan.crediya.r2dbc.repository;

import co.juan.crediya.model.dto.FilteredApplicationDto;
import co.juan.crediya.r2dbc.entity.ApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, Long>, ReactiveQueryByExampleExecutor<ApplicationEntity> {

    @Query("""
             SELECT
                 a.amount,
                 a.term,
                 a.email,
                 t.name AS loanType,
                 t.interest_rate AS interestRate,
                 s.name AS status
             FROM applications a
             INNER JOIN loan_type t ON a.id_loan_type = t.id_loan_type
             INNER JOIN states s ON a.id_state = s.id_state
             WHERE a.id_state = :status
             OFFSET :offset LIMIT :limit
            """)
    Flux<FilteredApplicationDto> findAllByPage(long status, long offset, int limit);

    @Query("SELECT COUNT(*) FROM applications a WHERE a.id_state = :status")
    Mono<Long> countAll(long status);
}
