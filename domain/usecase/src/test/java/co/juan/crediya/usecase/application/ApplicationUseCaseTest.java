package co.juan.crediya.usecase.application;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.application.gateways.ApplicationRepository;
import co.juan.crediya.model.debtCapacity.DebtCapacityGateway;
import co.juan.crediya.model.dto.*;
import co.juan.crediya.model.exceptions.CrediYaException;
import co.juan.crediya.model.exceptions.ErrorCode;
import co.juan.crediya.model.loantype.LoanType;
import co.juan.crediya.model.notification.NotificationGateway;
import co.juan.crediya.model.user.User;
import co.juan.crediya.model.user.UserGateway;
import co.juan.crediya.usecase.loantype.LoanTypeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUseCaseTest {

    @InjectMocks
    private ApplicationUseCase applicationUseCase;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private LoanTypeUseCase loanTypeUseCase;

    @Mock
    private UserGateway userGateway;

    @Mock
    NotificationGateway notificationGateway;

    @Mock
    DebtCapacityGateway debtCapacityGateway;

    private LoanType loanType;
    private Application application;
    private LoanApplicationDTO loanApplicationDTO;
    private User user;

    private final FilteredApplicationDto filteredApplicationDto =
            new FilteredApplicationDto(1L, new BigDecimal("1000"), 12,
                    "juan.juan@gmail.com", "Pedro",
                    "Libre inversion", new BigDecimal(2),
                    "Pendiente de revision", new BigDecimal(10000),
                    new BigDecimal(100));
    private final long status = 1L;
    private UpdateLoanApplicationRequestDto updateLoanApplicationRequestDto;
    private AutomaticValidationDto automaticValidationDto;

    @BeforeEach
    void initMocks() {
        application = new Application();
        application.setIdApplication(1L);
        application.setAmount(BigDecimal.TEN);
        application.setTerm(12);
        application.setEmail("juan.ceballos@correo.com");
        application.setIdState(1L);
        application.setIdLoanType(1L);

        loanType = new LoanType(
                1L,
                "Libre Inversion",
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.ONE,
                true
        );

        loanApplicationDTO = new LoanApplicationDTO();
        loanApplicationDTO.setDni("1027384098");
        loanApplicationDTO.setIdLoanType(1L);
        loanApplicationDTO.setTerm(12);
        loanApplicationDTO.setAmount(BigDecimal.TEN);
        loanApplicationDTO.setEmailLogged("juan.ceballos@correo.com");

        user = new User();
        user.setName("Juan");
        user.setLastName("Ceballos");
        user.setBirthDate(LocalDateTime.of(2025, 8, 25, 20, 46));
        user.setEmail("juan.ceballos@correo.com");
        user.setAddress("CRA 97 AA #55-33");
        user.setBaseSalary(BigDecimal.TEN);
        user.setPhone("3210938475");
        user.setDni("1027384098");
        user.setRole(1L);

        updateLoanApplicationRequestDto = new UpdateLoanApplicationRequestDto();
        updateLoanApplicationRequestDto.setIdApplication(19L);
        updateLoanApplicationRequestDto.setIdState(4L);

        automaticValidationDto = new AutomaticValidationDto();
        automaticValidationDto.setApplicationId(1L);
        automaticValidationDto.setApplicantEmail("juandceballos12@gmail.com");
        automaticValidationDto.setApplicantSalary(BigDecimal.TEN);
        automaticValidationDto.setNewLoanAmount(BigDecimal.ONE);
        automaticValidationDto.setNewLoanInterestRate(BigDecimal.ONE);
        automaticValidationDto.setNewLoanTerm(12);
        automaticValidationDto.setActiveLoans(List.of(filteredApplicationDto));
    }

    @Test
    void saveApplication_shouldSave() {
        loanType = new LoanType(
                1L,
                "Libre Inversion",
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.ONE,
                true
        );

        when(loanTypeUseCase.getLoanTypeById(anyLong())).thenReturn(Mono.just(loanType));
        when(applicationRepository.saveApplication(any(Application.class))).thenReturn(Mono.just(application));
        when(userGateway.getUserByDni(anyString())).thenReturn(Mono.just(user));
        when(applicationRepository.getApplicationsByUserEmailAndState(anyString(), anyLong())).thenReturn(Flux.just(filteredApplicationDto));
        when(debtCapacityGateway.sendValidationMessage(any(AutomaticValidationDto.class))).thenReturn(Mono.empty());

        Mono<Application> response = applicationUseCase.saveApplication(loanApplicationDTO);

        StepVerifier.create(response)
                .expectNextMatches(value -> value.equals(application))
                .verifyComplete();

        verify(loanTypeUseCase, times(1)).getLoanTypeById(anyLong());
        verify(applicationRepository, times(1)).saveApplication(any(Application.class));
        verify(userGateway, times(1)).getUserByDni(anyString());
        verify(applicationRepository, times(1)).getApplicationsByUserEmailAndState(anyString(), anyLong());
    }

    @Test
    void saveApplication_shouldThrowWhenUserNotFound() {
        when(userGateway.getUserByDni(anyString())).thenReturn(Mono.empty());
        when(loanTypeUseCase.getLoanTypeById(anyLong())).thenReturn(Mono.just(loanType));

        Mono<Application> response = applicationUseCase.saveApplication(loanApplicationDTO);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof CrediYaException &&
                        ((CrediYaException) throwable).getErrorCode() == ErrorCode.USER_NOT_FOUND)
                .verify();

        verify(userGateway, times(1)).getUserByDni(anyString());
        verify(applicationRepository, never()).saveApplication(any());
    }

    @Test
    void saveApplication_shouldThrowWhenInvalidLoanType() {
        when(userGateway.getUserByDni(anyString())).thenReturn(Mono.just(user));
        when(loanTypeUseCase.getLoanTypeById(anyLong())).thenReturn(Mono.empty());

        Mono<Application> response = applicationUseCase.saveApplication(loanApplicationDTO);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof CrediYaException &&
                        ((CrediYaException) throwable).getErrorCode() == ErrorCode.INVALID_LOAN_TYPE)
                .verify();

        verify(loanTypeUseCase, times(1)).getLoanTypeById(anyLong());
        verify(applicationRepository, never()).saveApplication(any());
    }

    @Test
    void getAllApplications_shouldReturnSomething() {
        List<FilteredApplicationDto> applications = List.of(filteredApplicationDto);

        when(applicationRepository.findAllApplicationsPaging(anyLong(), anyLong(), anyInt())).thenReturn(Mono.just(applications));
        when(userGateway.getUserByEmail(anyString())).thenReturn(Mono.just(user));

        long offset = 0L;
        int limit = 5;
        Mono<List<FilteredApplicationDto>> response = applicationUseCase.getAllApplicationsPaging(status, offset, limit);

        StepVerifier.create(response)
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    FilteredApplicationDto dto = list.get(0);
                    assertThat(dto.name()).isEqualTo("Juan");
                    assertThat(dto.baseSalary()).isEqualTo(BigDecimal.TEN);
                    assertThat(dto.monthlyRequestAmount()).isEqualTo(new BigDecimal("166"));
                })
                .verifyComplete();

        verify(applicationRepository, times(1)).findAllApplicationsPaging(anyLong(), anyLong(), anyInt());
        verify(userGateway, times(1)).getUserByEmail(anyString());
    }

    @Test
    void countAll() {
        Long allRows = 21L;
        when(applicationRepository.countAll(anyLong())).thenReturn(Mono.just(allRows));

        Mono<Long> response = applicationUseCase.countAll(status);

        StepVerifier.create(response)
                .expectNextMatches(value -> value.equals(allRows))
                .verifyComplete();
    }

    @Test
    void updateLoanApplication() {
        when(applicationRepository.findApplicationById(anyLong())).thenReturn(Mono.just(application));
        when(userGateway.getUserByEmail(anyString())).thenReturn(Mono.just(user));
        when(loanTypeUseCase.getLoanTypeById(anyLong())).thenReturn(Mono.just(loanType));
        when(applicationRepository.updateLoanApplication(any(UpdateLoanApplicationRequestDto.class)))
                .thenReturn(Mono.just(application));
        when(notificationGateway.sendNotification(any(FilteredApplicationDto.class))).thenReturn(Mono.empty());

        Mono<FilteredApplicationDto> response = applicationUseCase.updateApplication(updateLoanApplicationRequestDto);

        StepVerifier.create(response)
                .expectNextMatches(value -> value.amount().equals(application.getAmount())
                        && value.status().equalsIgnoreCase(Objects.requireNonNull(StatusEnum.getById(updateLoanApplicationRequestDto.getIdState())).getName()))
                .verifyComplete();

        verify(applicationRepository, times(1)).findApplicationById(anyLong());
        verify(userGateway, times(1)).getUserByEmail(anyString());
        verify(loanTypeUseCase, times(1)).getLoanTypeById(anyLong());
        verify(applicationRepository, times(1)).updateLoanApplication(any(UpdateLoanApplicationRequestDto.class));
    }

    @Test
    void updateLoanApplication_returnExceptionLoanApplication() {
        application.setIdLoanType(5L);

        when(applicationRepository.findApplicationById(anyLong())).thenThrow(new CrediYaException(ErrorCode.INVALID_LOAN_TYPE));

        Executable executable = () -> applicationUseCase.updateApplication(updateLoanApplicationRequestDto);

        CrediYaException exception = assertThrows(CrediYaException.class, executable);
        assertEquals("There's not an application with that id", exception.getMessage());

        verify(applicationRepository, times(1)).findApplicationById(anyLong());
        verify(applicationRepository, times(0)).updateLoanApplication(any(UpdateLoanApplicationRequestDto.class));
    }

    @Test
    void updateLoanApplication_returnExceptionStatus() {
        when(applicationRepository.findApplicationById(anyLong())).thenThrow(new CrediYaException(ErrorCode.STATUS_NOT_CHANGED));

        Executable executable = () -> applicationUseCase.updateApplication(updateLoanApplicationRequestDto);

        CrediYaException exception = assertThrows(CrediYaException.class, executable);
        assertEquals("The application with this id already has this status.", exception.getMessage());

        verify(applicationRepository, times(1)).findApplicationById(anyLong());
        verify(applicationRepository, times(0)).updateLoanApplication(any(UpdateLoanApplicationRequestDto.class));
    }
}