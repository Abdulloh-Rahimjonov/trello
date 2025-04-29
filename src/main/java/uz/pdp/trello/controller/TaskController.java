package uz.pdp.trello.controller;

import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.trello.entity.Attachment;
import uz.pdp.trello.entity.Status;
import uz.pdp.trello.entity.Task;
import uz.pdp.trello.entity.User;
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
                for (Status status : all) {
                    if (status.getPositionNumber() <= num){
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
    public String addTaskPage(Model model , @RequestParam Integer userId) {
        model.addAttribute("userId", userId);
        return "taskAddPage";
    }
    @Transactional
    @PostMapping("/addTask")
    public String addTask(@RequestParam Integer userId,
                          @RequestParam String title,
                          @RequestParam MultipartFile photo,
                          Model model) throws IOException {

        Optional<User> user = userRepository.findById(userId);
        Optional<Status> status = statusRepository.findByName("OPEN");

        if (user.isEmpty() || status.isEmpty()) {
            model.addAttribute("error", "User or Status not found");
            return "taskHomePage";
        }

        Attachment attachment = new Attachment();
        attachment.setContent(photo.getBytes());
        attachment.setFile_type(".jpg");
        attachmentRepository.save(attachment);

        Task task = new Task();
        task.setUser(user.get());
        task.setStatus(status.get());
        task.setTitle(title);
        task.setAttachment(attachment);

        taskRepository.save(task);

        return "redirect:/task";
    }

}
