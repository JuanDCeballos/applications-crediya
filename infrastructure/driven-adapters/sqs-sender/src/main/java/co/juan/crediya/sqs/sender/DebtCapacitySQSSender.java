package co.juan.crediya.sqs.sender;

import co.juan.crediya.model.debtCapacity.DebtCapacityGateway;
import co.juan.crediya.model.dto.AutomaticValidationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class DebtCapacitySQSSender implements DebtCapacityGateway {

    private final SQSSender sqsSender;
    private final ObjectMapper objectMapper;

    @Value("${adapter.sqs.debtCapacityQueueUrl}")
    private String validationQueueUrl;

    @Override
    public Mono<Void> sendValidationMessage(AutomaticValidationDto validationDto) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(validationDto))
                .flatMap(message -> sqsSender.send(validationQueueUrl, message))
                .then();
    }
}
