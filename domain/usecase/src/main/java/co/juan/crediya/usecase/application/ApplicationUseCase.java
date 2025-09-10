package co.juan.crediya.usecase.application;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.application.gateways.ApplicationRepository;
import co.juan.crediya.model.dto.FilteredApplicationDto;
import co.juan.crediya.model.dto.LoanApplicationDTO;
import co.juan.crediya.model.dto.StatusEnum;
import co.juan.crediya.model.dto.UpdateLoanApplicationRequestDto;
import co.juan.crediya.model.exceptions.CrediYaException;
import co.juan.crediya.model.exceptions.ErrorCode;
import co.juan.crediya.model.loantype.LoanType;
import co.juan.crediya.model.user.User;
import co.juan.crediya.model.user.UserGateway;
import co.juan.crediya.usecase.loantype.LoanTypeUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ApplicationUseCase {

    private final ApplicationRepository applicationRepository;
    private final LoanTypeUseCase loanTypeUseCase;
    private final UserGateway userGateway;

    public Mono<Application> saveApplication(LoanApplicationDTO loanApplicationDTO) {

        Mono<User> userMono = userGateway.getUserByDni(loanApplicationDTO.getDni());
        Mono<Boolean> loanTypeMono = loanTypeUseCase.getLoanTypeById(loanApplicationDTO.getIdLoanType()).hasElement();

        return Mono.zip(userMono, loanTypeMono)
                .filter(tuple -> true)
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.USER_NOT_FOUND)))
                .filter(email -> email.getT1().getEmail().equalsIgnoreCase(loanApplicationDTO.getEmailLogged()))
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.USER_NOT_MATCH)))
                .filter(Tuple2::getT2)
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.INVALID_LOAN_TYPE)))
                .flatMap(tuple ->
                        applicationRepository.saveApplication(Application.builder()
                                .email(tuple.getT1().getEmail())
                                .term(loanApplicationDTO.getTerm())
                                .amount(loanApplicationDTO.getAmount())
                                .idLoanType(loanApplicationDTO.getIdLoanType())
                                .idState(1L).build())
                );
    }

    public Mono<List<FilteredApplicationDto>> getAllApplicationsPaging(long status, long offset, int limit) {
        Mono<List<FilteredApplicationDto>> listApplicationsMono = applicationRepository.findAllApplicationsPaging(status, offset, limit);

        return listApplicationsMono.flatMap(applications ->
                Flux.fromIterable(applications)
                        .flatMap(application ->
                                userGateway.getUserByEmail(application.email())
                                        .map(user ->
                                                new FilteredApplicationDto(
                                                        application.idapplication(),
                                                        application.amount(),
                                                        application.term(),
                                                        application.email(),
                                                        user.getName(),
                                                        application.loantype(),
                                                        application.interestrate(),
                                                        application.status(),
                                                        user.getBaseSalary(),
                                                        application.amount()
                                                                .multiply(application.interestrate())
                                                                .divide(new BigDecimal(application.term()), RoundingMode.FLOOR)
                                                )
                                        )
                        )
                        .collectList()
        );
    }

    public Mono<Long> countAll(long status) {
        return applicationRepository.countAll(status);
    }

    public Mono<FilteredApplicationDto> updateApplication(UpdateLoanApplicationRequestDto updateLoanApplicationRequestDto) {
        return applicationRepository.findApplicationById(updateLoanApplicationRequestDto.getIdApplication())
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.INVALID_LOAN_TYPE)))
                .filter(application -> application.getIdState().compareTo(updateLoanApplicationRequestDto.getIdState()) != 0)
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.STATUS_NOT_CHANGED)))
                .flatMap(application -> {

                    Mono<User> userDtoMono = userGateway.getUserByEmail(application.getEmail());
                    Mono<LoanType> loanTypeMono = loanTypeUseCase.getLoanTypeById(application.getIdLoanType());
                    Mono<Application> applicationMono = applicationRepository.updateLoanApplication(updateLoanApplicationRequestDto);

                    return Mono.zip(applicationMono, userDtoMono, loanTypeMono)
                            .flatMap(tupleData -> {

                                Application applicationUpdated = tupleData.getT1();

                                return Mono.just(new FilteredApplicationDto(
                                        applicationUpdated.getIdApplication(),
                                        applicationUpdated.getAmount(),
                                        applicationUpdated.getTerm(),
                                        applicationUpdated.getEmail(),
                                        tupleData.getT2().getName(),
                                        applicationUpdated.getIdLoanType().toString(),
                                        tupleData.getT3().getInterestRate(),
                                        StatusEnum.getById(updateLoanApplicationRequestDto.getIdState()).getName(),
                                        tupleData.getT2().getBaseSalary(),
                                        applicationUpdated.getAmount()
                                                .multiply(tupleData.getT3().getInterestRate())
                                                .divide(new BigDecimal(applicationUpdated.getTerm()), RoundingMode.FLOOR)
                                ));
                            });
                });
    }
}
