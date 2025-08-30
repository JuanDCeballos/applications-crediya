package co.juan.crediya.constants;

import lombok.Getter;

@Getter
public enum OperationMessages {
    REQUEST_RECEIVED("Request received for entity {}"),
    SAVE_OPERATION_SUCCESS("Entity saved successfully {}"),
    SAVE_OPERATION_ERROR("Error while saving entity {}"),
    RECORD_CREATED_SUCCESSFULLY("Record created successfully");


    private final String message;

    OperationMessages(String message) {
        this.message = message;
    }
}
