package uz.pdp.trello.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.trello.entity.Status;
import uz.pdp.trello.entity.Task;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.repo.StatusRepository;
import uz.pdp.trello.repo.TaskRepository;
import uz.pdp.trello.repo.UserRepository;

import java.util.List;

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
        List<Status> status = statusRepository.findAll();
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("status", status);
        model.addAttribute("user", user);

        return "task"; //Iskandar oka siz html yaratib html nomini shu joyga qoshas
    }
}
