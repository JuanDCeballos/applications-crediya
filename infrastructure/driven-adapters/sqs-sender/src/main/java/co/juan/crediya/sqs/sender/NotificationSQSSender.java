package co.juan.crediya.sqs.sender;

import co.juan.crediya.model.dto.FilteredApplicationDto;
import co.juan.crediya.model.notification.NotificationGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotificationSQSSender implements NotificationGateway {

    private final SQSSender sqsSender;
    private final ObjectMapper objectMapper;

    @Value("${adapter.sqs.notificationQueueUrl}")
    private String notificationQueueUrl;

    @Override
    public Mono<Void> sendNotification(FilteredApplicationDto filteredApplicationDto) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(filteredApplicationDto))
                .flatMap(message -> sqsSender.send(notificationQueueUrl, message))
                .then();
    }
}
