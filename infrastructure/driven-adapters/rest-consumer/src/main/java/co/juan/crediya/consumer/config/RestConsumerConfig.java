package co.juan.crediya.consumer.config;

import co.juan.crediya.security.SecurityContextRepository;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
public class RestConsumerConfig {

    private final SecurityContextRepository securityContextRepository;

    private final String url;

    private final int timeout;

    public RestConsumerConfig(SecurityContextRepository securityContextRepository, @Value("${adapter.restconsumer.url}") String url,
                              @Value("${adapter.restconsumer.timeout}") int timeout) {
        this.securityContextRepository = securityContextRepository;
        this.url = url;
        this.timeout = timeout;
    }

    @Bean
    public WebClient getWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .clientConnector(getClientHttpConnector())
                .filter(addJwtTokenToHeader())
                .build();
    }

    private ClientHttpConnector getClientHttpConnector() {
        return new ReactorClientHttpConnector(HttpClient.create()
                .compress(true)
                .keepAlive(true)
                .option(CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
                }));
    }

    private ExchangeFilterFunction addJwtTokenToHeader() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> Mono.just(ClientRequest.from(request)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + securityContextRepository.getToken())
                        .build())
                .defaultIfEmpty(request));
    }

}
