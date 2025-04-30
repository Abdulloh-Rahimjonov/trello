package uz.pdp.trello.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.trello.entity.Role;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.repo.RoleRepository;
import uz.pdp.trello.repo.UserRepository;


import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
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
    public String changeUserRole(@PathVariable Integer id, @RequestParam Integer roleId) {
        User user = userRepository.findById(id).orElseThrow();
        Role role = roleRepository.findById(roleId).orElseThrow();
        user.getRoles().clear();
        user.getRoles().add(role);
        userRepository.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/add-role/{id}")
    public String addRole(@PathVariable Integer id, @RequestParam Integer roleId) {
        User user = userRepository.findById(id).orElseThrow();
        Role role = roleRepository.findById(roleId).orElseThrow();
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
        return "redirect:/admin/change-role/" + id;
    }
}
