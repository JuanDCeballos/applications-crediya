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

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final ApplicationPath applicationPath;

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/api/v1/solicitud", method = RequestMethod.GET, beanClass = Handler.class, beanMethod = "listenGetAllApplications"),
            @RouterOperation(path = "/api/v1/solicitud", method = RequestMethod.POST, beanClass = Handler.class, beanMethod = "listenSaveApplication"),
            @RouterOperation(path = "/api/v1/solicitud", method = RequestMethod.PUT, beanClass = Handler.class, beanMethod = "listenPutApplication")
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(applicationPath.getSolicitud()), handler::listenSaveApplication)
                .andRoute(GET(applicationPath.getSolicitud()), handler::listenGetAllApplications)
                .andRoute(PUT(applicationPath.getSolicitud()), handler::listenPutApplication);
    }
}
