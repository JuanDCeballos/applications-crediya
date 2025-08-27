package co.juan.crediya.model.application;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Application {
    
    private Long idApplication;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private Long idState;
    private Long idLoanType;
}
