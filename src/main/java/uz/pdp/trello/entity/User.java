package uz.pdp.trello.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String username;
    private String name;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    List<Role> roles;
    @ManyToOne(fetch = FetchType.EAGER)
    Attachment attachment;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }


}
