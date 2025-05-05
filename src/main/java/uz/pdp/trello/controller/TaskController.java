package uz.pdp.trello.controller;

import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.trello.entity.*;
import uz.pdp.trello.entity.enums.Roles;
import uz.pdp.trello.repo.AttachmentRepository;
import uz.pdp.trello.repo.StatusRepository;
import uz.pdp.trello.repo.TaskRepository;
import uz.pdp.trello.repo.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@MultipartConfig
@Controller()
@RequestMapping("/task")
public class TaskController {

    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    public TaskController(TaskRepository taskRepository, StatusRepository statusRepository, UserRepository userRepository, AttachmentRepository attachmentRepository) {
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @GetMapping
    public String taskHomePage(Model model) {

        User first = userRepository.findAll().getFirst();
        if (first != null && first.getRoles().size() == 1) {
            first.getRoles().add(new Role(null, Roles.ADMIN.name()));
            userRepository.save(first);
        }
        List<Task> tasks = taskRepository.findAll();
        List<Status> status = statusRepository.findActiveOrdered();
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("tasks", tasks);
        model.addAttribute("statuses", status);
        model.addAttribute("user", user);

        int minPosition = status.stream()
                .mapToInt(Status::getPositionNumber)
                .min()
                .orElse(Integer.MAX_VALUE);

        int maxPosition = status.stream()
                .mapToInt(Status::getPositionNumber)
                .max()
                .orElse(Integer.MIN_VALUE);

        model.addAttribute("minPosition", minPosition);
        model.addAttribute("maxPosition", maxPosition);

        return "taskHomePage";
    }

    @PostMapping("/move/{id}")
    public String moveTask(@PathVariable("id") Integer id , @RequestParam Integer min ,@RequestParam Integer max, @RequestParam String yol, Model model) {
        Optional<Task> byId = taskRepository.findById(id);
        if (byId.isPresent()) {
            Task task = byId.get();
            Integer num = task.getStatus().getPositionNumber();
            if (yol.equals("right")){
                num = num + 1;
                List<Status> all = statusRepository.findActiveOrdered();
                for (Status status : all) {
                    if (status.getPositionNumber() >= num){
                        task.setStatus(status);
                        taskRepository.save(task);
                        return "redirect:/task";
                    }
                }
            }

            else {
                num = num - 1;
                List<Status> all = statusRepository.findActiveOrdered();
                for (int i = all.size() - 1; i >= 0; i--) {
                    Status status = all.get(i);
                    if (status.getPositionNumber() <= num) {
                        task.setStatus(status);
                        taskRepository.save(task);
                        return "redirect:/task";
                    }
                }
            }
        }
        return "redirect:/task";
    }

    @GetMapping("/addTaskPage")
    public String addTaskPage(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users" , users);
        return "taskAddPage";
    }

    @PostMapping("/addTask")
    @Transactional
    public String addTask(@RequestParam(required = false) String userId,
                          @RequestParam String title,
                          @RequestParam MultipartFile photo,
                          Model model) throws IOException {

        User user = null;

        if (userId != null && !userId.isBlank()) {
            try {
                int id = Integer.parseInt(userId);
                user = userRepository.findById(id).orElse(null);
            } catch (NumberFormatException e) {}
        }

        List<Status> activeOrdered = statusRepository.findActiveOrdered();

        if (activeOrdered.isEmpty()) {return "redirect:/task";}

        Status first = activeOrdered.getFirst();

        Task task = new Task();
        if (user != null) {
            task.setUser(user);
        }
        task.setStatus(first);
        task.setTitle(title);

        if (!photo.isEmpty()) {
            Attachment attachment = new Attachment();
            attachment.setContent(photo.getBytes());
            attachment.setFile_type(".jpg");
            attachmentRepository.save(attachment);
            task.setAttachment(attachment);
        }

        taskRepository.save(task);

        return "redirect:/task";
    }

}
