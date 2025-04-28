package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.trello.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
