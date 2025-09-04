package co.juan.crediya.model.user;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String name;
    private String lastName;
    private LocalDateTime birthDate;
    private String address;
    private String phone;
    private String email;
    private BigDecimal baseSalary;
    private String dni;
    private Long role;
}
