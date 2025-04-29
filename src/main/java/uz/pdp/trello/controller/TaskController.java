package uz.pdp.trello.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.trello.entity.Status;
import uz.pdp.trello.entity.Task;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.repo.StatusRepository;
import uz.pdp.trello.repo.TaskRepository;
import uz.pdp.trello.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping("/task")
public class TaskController {

    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, StatusRepository statusRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String taskHomePage(Model model) {

        List<Task> tasks = taskRepository.findAll();
        List<Status> status = statusRepository.findActiveOrdered();
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
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
        return "";
    }
}
