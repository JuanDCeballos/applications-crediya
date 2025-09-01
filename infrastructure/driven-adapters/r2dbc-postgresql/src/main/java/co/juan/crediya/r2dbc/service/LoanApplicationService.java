package co.juan.crediya.r2dbc.service;

import co.juan.crediya.constants.OperationMessages;
import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.dto.LoanApplicationDTO;
import co.juan.crediya.security.JwtProvider;
import co.juan.crediya.security.SecurityContextRepository;
import co.juan.crediya.usecase.application.ApplicationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanApplicationService {

    private final TransactionalOperator transactionalOperator;
    private final ApplicationUseCase applicationUseCase;
    private final SecurityContextRepository securityContextRepository;
    private final JwtProvider jwtProvider;

    public Mono<Application> createApplication(LoanApplicationDTO loanApplicationDTO) {
        loanApplicationDTO.setEmailLogged(jwtProvider.getSubject(securityContextRepository.getToken()));
        return transactionalOperator.execute(transaction ->
                        applicationUseCase.saveApplication(loanApplicationDTO)
                )
                .doOnNext(applicationSaved -> log.info(OperationMessages.SAVE_OPERATION_SUCCESS.getMessage(), applicationSaved.toString()))
                .doOnError(throwable -> log.error(OperationMessages.SAVE_OPERATION_ERROR.getMessage(), throwable.getMessage()))
                .single();
    }

    public Mono<List<Application>> getAllApplications(long offset, int limit) {
        return applicationUseCase.getAllApplications(offset, limit);
    }

    public Mono<Long> countAll() {
        return applicationUseCase.countAll();
    }
}
