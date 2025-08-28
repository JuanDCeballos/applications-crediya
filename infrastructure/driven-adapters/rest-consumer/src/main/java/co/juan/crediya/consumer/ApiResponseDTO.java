package co.juan.crediya.consumer;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApiResponseDTO<T> {

    private Integer status;
    private String message;
    private List<String> errors;
    private T data;
}
