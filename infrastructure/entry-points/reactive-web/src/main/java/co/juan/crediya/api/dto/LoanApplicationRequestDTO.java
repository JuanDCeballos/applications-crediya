package co.juan.crediya.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationRequestDTO {

    @NotNull(message = "amount can't be null")
    private BigDecimal amount;
    @NotNull(message = "term can't be null")
    private Integer term;
    @NotEmpty(message = "email can't be empty")
    @Email(message = "Should be a valid email")
    private String email;
    @NotNull(message = "idLoanType can't be null")
    private Long idLoanType;
}
