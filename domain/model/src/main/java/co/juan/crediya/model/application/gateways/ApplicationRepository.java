package co.juan.crediya.model.application.gateways;

import co.juan.crediya.model.application.Application;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationRepository {

    Mono<Application> saveApplication(Application application);

    Flux<Application> findAllApplications();
}
