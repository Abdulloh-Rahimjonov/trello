package uz.pdp.trello.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.trello.entity.*;
import uz.pdp.trello.entity.enums.Roles;
import uz.pdp.trello.repo.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserCrudController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TaskRepository taskRepository;
    private final AttachmentRepository attachmentRepository;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @Transactional
    @PostMapping("/delete")
    public String deleteUser(Model model, @RequestParam int id, Principal principal) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // User bilan bog‘liq barcha tasklarni olib, userni null qilamiz
                List<Task> tasks = taskRepository.findAll();
                for (Task task : tasks) {
                    if (task.getUser() != null && task.getUser().equals(user)) {
                        task.setUser(null);
                    }
                }

                // Auth bo‘lgan user
                User thisUser = userRepository.findByUsername(principal.getName())
                        .orElseThrow();

                // Faqat agar attachment mavjud bo‘lsa, o‘chiramiz
                Attachment attachment = user.getAttachment();
                if (attachment != null) {
                    attachmentRepository.deleteById(attachment.getId());
                }

                taskRepository.saveAll(tasks);
                userRepository.deleteById(id);

                if (thisUser.equals(user)) {
                    return "redirect:/logout";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin";
        }

        return "redirect:/admin";
    }


    @GetMapping("/change-role/{id}")
    public String changeRolePage(@PathVariable Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "admin/change-role";
    }


    @PostMapping("/change-role/{id}")
    public String changeUserRoles(@PathVariable Integer id, @RequestParam List<Integer> roleId) {
        User user = userRepository.findById(id).orElseThrow();
        List<Role> roles = roleRepository.findAllById(roleId);

        user.setRoles(roles);
        userRepository.save(user);

        if (roles.size() == 1 && roles.get(0).getRole().equals(Roles.PROGRAMMER.name())) {
            return "redirect:/logout";
        }

        return "redirect:/admin";
    }

}
