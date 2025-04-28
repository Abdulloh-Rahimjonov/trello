package uz.pdp.trello.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User {

    @Id
    private int id;
    private String username;
    private String password;
    private String firstName;
}
