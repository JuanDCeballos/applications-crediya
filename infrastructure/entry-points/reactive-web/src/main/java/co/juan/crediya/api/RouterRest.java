package co.juan.crediya.api;

import co.juan.crediya.api.config.ApplicationPath;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final ApplicationPath applicationPath;

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/api/v1/solicitud", method = RequestMethod.GET, beanClass = Handler.class, beanMethod = "listenGetAllApplications"),
            @RouterOperation(path = "/api/v1/solicitud", method = RequestMethod.POST, beanClass = Handler.class, beanMethod = "listenSaveApplication")
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(applicationPath.getSolicitud()), handler::listenSaveApplication)
                .andRoute(GET(applicationPath.getSolicitud()), handler::listenGetAllApplications);
    }
}
