package co.juan.crediya.consumer;

import co.juan.crediya.model.exceptions.CrediYaException;
import co.juan.crediya.model.exceptions.ErrorCode;
import co.juan.crediya.model.user.User;
import co.juan.crediya.model.user.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UserGateway {

    private final WebClient client;

    @Value("${adapter.restconsumer.getUserByDni}")
    private String getUserByDni;

    @Value("${adapter.restconsumer.getUserByEmail}")
    private String getUserByEmail;


    @Override
    public Mono<User> getUserByDni(String dni) {
        return client
                .get()
                .uri(getUserByDni, dni)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new CrediYaException(ErrorCode.USER_NOT_FOUND));
                    } else {
                        return Mono.error(new CrediYaException(ErrorCode.DATABASE_ERROR));
                    }
                })
                .bodyToMono(new ParameterizedTypeReference<ApiResponseDTO<User>>() {
                })
                .filter(Objects::nonNull)
                .flatMap(apiResponseDTO ->
                        Mono.just(apiResponseDTO.getData())
                );
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        return client
                .get()
                .uri(getUserByEmail, email)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new CrediYaException(ErrorCode.USER_EMAIL_NOT_FOUND));
                    } else {
                        return Mono.error(new CrediYaException(ErrorCode.DATABASE_ERROR));
                    }
                })
                .bodyToMono(new ParameterizedTypeReference<ApiResponseDTO<User>>() {
                })
                .filter(Objects::nonNull)
                .flatMap(apiResponseDTO ->
                        Mono.just(apiResponseDTO.getData())
                );
    }
}
