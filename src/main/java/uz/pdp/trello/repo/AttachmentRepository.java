package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.trello.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
}
