package co.juan.crediya.model.exceptions;

import lombok.Getter;

@Getter
public class CrediYaException extends RuntimeException {

    private final ErrorCode errorCode;

    public CrediYaException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
