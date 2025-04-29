package uz.pdp.trello.repo;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.trello.entity.Status;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Integer> {

    @Query("SELECT s FROM Status s WHERE s.isActive = true ORDER BY s.positionNumber")
    List<Status> findActiveOrdered();

}