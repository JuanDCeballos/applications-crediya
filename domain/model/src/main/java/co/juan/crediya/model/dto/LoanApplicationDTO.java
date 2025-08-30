package co.juan.crediya.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoanApplicationDTO {

    private String dni;
    private BigDecimal amount;
    private Integer term;
    private Long idLoanType;
}
