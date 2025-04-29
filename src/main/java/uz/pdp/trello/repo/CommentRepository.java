package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.trello.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}