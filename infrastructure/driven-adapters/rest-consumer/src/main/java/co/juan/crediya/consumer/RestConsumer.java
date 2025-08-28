package co.juan.crediya.consumer;

import co.juan.crediya.model.exceptions.CrediYaException;
import co.juan.crediya.model.exceptions.ErrorCode;
import co.juan.crediya.model.user.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UserGateway {

    private final WebClient client;

    @Override
    public Mono<String> getUserEmailByDni(String dni) {
        return client
                .get()
                .uri("/dni/{dni}", dni)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new CrediYaException(ErrorCode.DATABASE_ERROR));
                    }
                })
                .bodyToMono(ApiResponseDTO.class)
                .flatMap(apiResponseDTO -> {
                    if (apiResponseDTO.getData() != null) {
                        return Mono.just(apiResponseDTO.getData().toString());
                    }

                    return Mono.just("");
                });
    }
}
