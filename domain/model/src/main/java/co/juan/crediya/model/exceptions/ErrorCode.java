package co.juan.crediya.model.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_LOAN_TYPE("BEC_NA", "There's not an application with that id", 409),
    DATABASE_ERROR("BEC_DBE", "An error has occurred while communicating with the database.", 500),
    USER_NOT_FOUND("BEC_NFU", "The user with dni doesn't exists.", 404),
    BAD_TOKEN("BEC_BT", "No token has been sent in the request.", 403),
    INVALID_TOKEN("BEC_IT", "Invalid authentication in the request.", 403),
    USER_NOT_MATCH("BEC_UNM", "Can't do a loan application for other user", 409);

    private final String businessErrorCode;
    private final String message;
    private final Integer httpCode;

    ErrorCode(String businessErrorCode, String message, Integer httpCode) {
        this.businessErrorCode = businessErrorCode;
        this.message = message;
        this.httpCode = httpCode;
    }
}
