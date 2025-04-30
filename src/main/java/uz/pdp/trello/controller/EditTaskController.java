package uz.pdp.trello.controller;

import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.trello.entity.Attachment;
import uz.pdp.trello.entity.Task;
import uz.pdp.trello.repo.AttachmentRepository;
import uz.pdp.trello.repo.TaskRepository;
import uz.pdp.trello.repo.UserRepository;
import java.io.IOException;
import java.util.Optional;

@MultipartConfig
@Controller
public class EditTaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    public EditTaskController(TaskRepository taskRepository, UserRepository userRepository, AttachmentRepository attachmentRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable int id, Model model) {
        Optional<Task> byId = taskRepository.findById(id);
        if (byId.isPresent()) {
            model.addAttribute("task", byId.get());
            model.addAttribute("users", userRepository.findAll());
            return "editTask";
        }
        return "redirect:/task";
    }

    @PostMapping("/edit")
    public String editTask(@ModelAttribute Task task,
                           @RequestParam("photo") MultipartFile file) {

        Optional<Task> optionalTask = taskRepository.findById(task.getId());
        if (optionalTask.isEmpty()) {
            return "redirect:/task";
        }

        Task existingTask = optionalTask.get();
        existingTask.setTitle(task.getTitle());
        existingTask.setUser(task.getUser());

        if (!file.isEmpty()) {
            try {
                Attachment attachment = new Attachment();
                attachment.setContent(file.getBytes());
                attachment.setFile_type(file.getContentType());
                attachmentRepository.save(attachment);
                existingTask.setAttachment(attachment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        taskRepository.save(existingTask);
        return "redirect:/task";
    }

}
