package co.juan.crediya.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoanApplicationRequestDTO {

    @Min(value = 0, message = "Amount should be positive")
    @NotNull(message = "amount can't be null")
    private BigDecimal amount;
    @NotNull(message = "term can't be null")
    private Integer term;
    @NotEmpty(message = "dni can't be empty")
    private String dni;
    @NotNull(message = "idLoanType can't be null")
    private Long idLoanType;
}
