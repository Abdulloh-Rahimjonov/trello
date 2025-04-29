package uz.pdp.trello.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Status {

    @Id
    private Integer id;

    @Column(unique=true)
    private String name;
    private Boolean isActive;
    @Column(unique = true)
    private Integer positionNumber;
}
