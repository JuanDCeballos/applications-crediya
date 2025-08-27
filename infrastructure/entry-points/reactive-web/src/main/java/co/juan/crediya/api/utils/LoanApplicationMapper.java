package co.juan.crediya.api.utils;

import co.juan.crediya.api.dto.LoanApplicationRequestDTO;
import co.juan.crediya.model.application.Application;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    Application toApplication(LoanApplicationRequestDTO loanApplicationRequestDto);

    LoanApplicationRequestDTO toLoanApplicationRequestDto(Application user);
}
