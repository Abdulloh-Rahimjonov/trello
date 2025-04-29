package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.trello.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}