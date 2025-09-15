package co.juan.crediya.model.debtCapacity;

import co.juan.crediya.model.dto.AutomaticValidationDto;
import reactor.core.publisher.Mono;

public interface DebtCapacityGateway {
    Mono<Void> sendValidationMessage(AutomaticValidationDto validationDto);
}
