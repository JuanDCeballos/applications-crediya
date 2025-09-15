package co.juan.crediya.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AutomaticValidationDto {
    private Long applicationId;
    private BigDecimal newLoanAmount;
    private Integer newLoanTerm;
    private BigDecimal newLoanInterestRate;
    private String applicantEmail;
    private BigDecimal applicantSalary;
    private List<FilteredApplicationDto> activeLoans;
}
