package co.juan.crediya.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateLoanApplicationRequestDto {

    private Long idApplication;
    private Long idState;
}
