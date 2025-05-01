package uz.pdp.trello.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.trello.entity.Comment;
import uz.pdp.trello.entity.Task;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.repo.CommentRepository;
import uz.pdp.trello.repo.TaskRepository;
import uz.pdp.trello.repo.UserRepository;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public String getCommentPage(@PathVariable Integer id, Model model) {
        Optional<Task> byId = taskRepository.findById(id);
        if (byId.isPresent()) {
            Task task = byId.get();
            model.addAttribute("task", task);
            return "comments";
        }
        return "redirect:/task";
    }

    @PostMapping("/add/{taskId}")
    public String addComment(@PathVariable Integer taskId,
                             @RequestParam String text,
                             Principal principal, Model model) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            return "redirect:/task";
        }

        Task task = taskOpt.get();
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        Comment comment = new Comment();
        comment.setCreatedAt(LocalDateTime.now());
        comment.setText(text);
        comment.setUserName(user.getName());

        commentRepository.save(comment);
        task.getComments().add(comment);
        taskRepository.save(task);

        model.addAttribute("task", task);

        return "comments";

    }
}
