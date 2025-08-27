package co.juan.crediya.r2dbc.dto;

import co.juan.crediya.model.application.Application;
import co.juan.crediya.model.loantype.LoanType;
import co.juan.crediya.model.states.States;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationResponse {

    private Application application;
    private LoanType loanType;
    private States states;

}
