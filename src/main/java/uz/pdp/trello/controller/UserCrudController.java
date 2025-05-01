package uz.pdp.trello.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.trello.entity.*;
import uz.pdp.trello.entity.enums.Roles;
import uz.pdp.trello.repo.*;
import java.util.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserCrudController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        try {
            userRepository.deleteById(id);
        }catch (Exception e) {
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
