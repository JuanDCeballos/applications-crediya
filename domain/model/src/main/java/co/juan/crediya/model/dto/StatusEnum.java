package co.juan.crediya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    APPROVED(4, "Aprobada"),
    REJECTED(2, "Rechazada");

    private final long id;
    private final String name;

    public static StatusEnum getById(long id) {
        for (StatusEnum status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        return null;
    }
}
