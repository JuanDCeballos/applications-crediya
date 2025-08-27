package co.juan.crediya.r2dbc.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ApiResponseDTO<T> {

    private Integer status;
    private String message;
    private List<String> errors;
    private T data;
}
