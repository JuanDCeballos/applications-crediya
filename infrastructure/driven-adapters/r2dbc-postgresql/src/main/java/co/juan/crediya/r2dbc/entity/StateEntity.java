package co.juan.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("states")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StateEntity {

    @Id
    @Column("id_status")
    private Long id;

    private String name;
    private String description;
}
