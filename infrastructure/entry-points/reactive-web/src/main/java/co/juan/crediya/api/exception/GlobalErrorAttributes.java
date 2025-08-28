package co.juan.crediya.api.exception;

import co.juan.crediya.model.exceptions.CrediYaException;
import jakarta.validation.ValidationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        Throwable error = getError(request);

        int status = determineHttpStatus(error);
        errorAttributes.put("status", status);
        errorAttributes.put("message", error.getMessage());
        errorAttributes.put("path", request.path());
        errorAttributes.put("error", HttpStatus.valueOf(status).getReasonPhrase());

        return errorAttributes;
    }

    private int determineHttpStatus(Throwable error) {
        if (error instanceof CrediYaException crediYaException) {
            return crediYaException.getErrorCode().getCode();
        } else if (error instanceof IllegalArgumentException || error instanceof ValidationException) {
            return HttpStatus.BAD_REQUEST.value();
        } else if (error instanceof IllegalStateException) {
            return HttpStatus.CONFLICT.value();
        } else if (error instanceof RuntimeException) {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
