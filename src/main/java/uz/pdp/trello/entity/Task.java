package uz.pdp.trello.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Status status;
    @ManyToOne
    private Attachment attachment;
    @ManyToOne
    private User user;
    private String title;
    @OneToMany
    List<Comment> comments;
}
