package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.trello.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
