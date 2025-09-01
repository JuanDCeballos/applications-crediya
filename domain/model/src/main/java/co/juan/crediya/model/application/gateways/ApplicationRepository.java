package co.juan.crediya.model.application.gateways;

import co.juan.crediya.model.application.Application;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ApplicationRepository {

    Mono<Application> saveApplication(Application application);

    Mono<List<Application>> findAllApplications(long offset, int limit);

    Mono<Long> countAll();
}
