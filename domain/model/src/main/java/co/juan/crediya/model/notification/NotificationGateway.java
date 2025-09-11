package co.juan.crediya.model.notification;

import co.juan.crediya.model.dto.FilteredApplicationDto;
import reactor.core.publisher.Mono;

public interface NotificationGateway {
    Mono<Void> sendNotification(FilteredApplicationDto filteredApplicationDto);
}
