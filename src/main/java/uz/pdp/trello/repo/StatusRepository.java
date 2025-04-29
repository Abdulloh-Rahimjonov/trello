package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.trello.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}