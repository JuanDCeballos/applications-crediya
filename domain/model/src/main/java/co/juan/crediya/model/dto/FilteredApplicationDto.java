package co.juan.crediya.model.dto;

import java.math.BigDecimal;

public record FilteredApplicationDto(Long idapplication,
                                     BigDecimal amount,
                                     int term,
                                     String email,
                                     String name,
                                     String loantype,
                                     BigDecimal interestrate,
                                     String status,
                                     BigDecimal baseSalary,
                                     BigDecimal monthlyRequestAmount) {
}
