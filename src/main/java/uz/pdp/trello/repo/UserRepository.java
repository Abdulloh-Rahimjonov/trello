package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.trello.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}
