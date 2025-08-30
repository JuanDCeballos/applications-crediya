package co.juan.crediya.api;

import co.juan.crediya.api.dto.LoanApplicationRequestDTO;
import co.juan.crediya.api.utils.LoanApplicationMapper;
import co.juan.crediya.api.utils.ValidationService;
import co.juan.crediya.constants.OperationMessages;
import co.juan.crediya.model.application.Application;
import co.juan.crediya.r2dbc.dto.ApiResponseDTO;
import co.juan.crediya.r2dbc.service.LoanApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final LoanApplicationService loanApplicationService;
    private final ValidationService validationService;
    private final LoanApplicationMapper loanApplicationMapper;

    @Operation(
            operationId = "saveApplication",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Fields empty or null",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    ),
            },
            requestBody = @RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = Application.class)
                    )
            )
    )
    public Mono<ServerResponse> listenSaveApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoanApplicationRequestDTO.class)
                .flatMap(validationService::validateObject)
                .doOnNext(req -> log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), req.toString()))
                .map(loanApplicationMapper::toApplication)
                .flatMap(loanApplicationService::createApplication)
                .flatMap(savedApplication -> {
                    ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                            .status("201")
                            .message(OperationMessages.RECORD_CREATED_SUCCESSFULLY.getMessage())
                            .data(savedApplication).build();
                    return ServerResponse.status(201).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                });
    }

    @Operation(
            operationId = "getAllApplications",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    )
            }
    )
    public Mono<ServerResponse> listenGetAllApplications(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("");
    }
}
