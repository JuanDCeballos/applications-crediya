package co.juan.crediya.model.application.gateways;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.dto.FilteredApplicationDto;
import co.juan.crediya.model.dto.UpdateLoanApplicationRequestDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ApplicationRepository {

    Mono<Application> saveApplication(Application application);

    Mono<List<FilteredApplicationDto>> findAllApplicationsPaging(long status, long offset, int limit);

    Mono<Long> countAll(long status);

    Mono<Application> findApplicationById(Long idApplication);

    Mono<Application> updateLoanApplication(UpdateLoanApplicationRequestDto updateLoanApplicationRequestDto);
}
