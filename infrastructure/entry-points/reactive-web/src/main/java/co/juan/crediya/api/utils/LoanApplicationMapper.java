package co.juan.crediya.api.utils;

import co.juan.crediya.api.dto.LoanApplicationRequestDTO;
import co.juan.crediya.api.dto.UpdateApplicatonRequestDto;
import co.juan.crediya.model.dto.LoanApplicationDTO;
import co.juan.crediya.model.dto.UpdateLoanApplicationRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    LoanApplicationDTO toApplication(LoanApplicationRequestDTO loanApplicationRequestDto);

    LoanApplicationRequestDTO toLoanApplicationRequestDto(LoanApplicationDTO user);

    UpdateApplicatonRequestDto toUpdateApplication(UpdateLoanApplicationRequestDto loanApplicationRequestDtoDto);

    UpdateLoanApplicationRequestDto toUpdateLoanApplication(UpdateApplicatonRequestDto updateApplicatonRequestDto);
}
