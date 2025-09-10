package co.juan.crediya.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateApplicatonRequestDto {

    @Min(value = 1, message = "IdApplication should be positive")
    @NotNull(message = "IdApplication can't be null")
    private Long idApplication;

    @Min(value = 1, message = "IdApplication should be positive")
    @NotNull(message = "IdState can't be null")
    private Long idState;
}
