package co.juan.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("applications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApplicationEntity {

    @Id
    @Column("id_application")
    private Long idApplication;

    private BigDecimal amount;
    private Integer term;
    private String email;

    @Column("id_state")
    private Long idState;

    @Column("id_loan_type")
    private Long idLoanType;
}
