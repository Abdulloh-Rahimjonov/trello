package uz.pdp.trello.controller;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.trello.dto.UserDTO;
import uz.pdp.trello.entity.Attachment;
import uz.pdp.trello.entity.Role;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.entity.enums.Roles;
import uz.pdp.trello.repo.AttachmentRepository;
import uz.pdp.trello.repo.RoleRepository;
import uz.pdp.trello.repo.UserRepository;
import uz.pdp.trello.service.EmailService;

import javax.xml.transform.sax.SAXResult;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
@MultipartConfig
@Controller
@RequiredArgsConstructor
public class VerifiedController {

    private final EmailService emailService;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/verifiedPage")
    public String getVerified(@ModelAttribute UserDTO userDTO, Model model) {
        String randomCode = emailService.randomCode();
        emailService.sendEmail(userDTO.getUsername() , randomCode);
        model.addAttribute("randomCode", randomCode);
        model.addAttribute("userDTO", userDTO);
        return "verified";
    }

    @SneakyThrows
    @PostMapping("/verified")
    public String verified(@RequestParam String randomCode,
                           @ModelAttribute UserDTO userDTO,
                           @RequestParam String userCode,
                           Model model) {
        if (!randomCode.equals(userCode)) {
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("randomCode", randomCode);
            return "redirect:/verifiedPage";
        }

        if (userDTO.getPassword().equals(userDTO.getPassword2())) {
            User user = new User();
            user.setName(userDTO.getName());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setUsername(userDTO.getUsername());

            Role byName = roleRepository.findByRole(Roles.PROGRAMMER.toString());
            List<Role> roles = new ArrayList<>();
            roles.add(byName);

            Attachment attachment;
            if (userDTO.getPhoto() != null && !userDTO.getPhoto().isEmpty()) {
                // Userdan keldi
                attachment = new Attachment();
                attachment.setContent(userDTO.getPhoto().getBytes());
            } else {
                attachment = new Attachment();
                InputStream inputStream = getClass().getResourceAsStream("/static/picture/rasm.jpg");
                if (inputStream != null) {
                    attachment.setContent(inputStream.readAllBytes());
                } else {
                    throw new RuntimeException("Default picture not found!");
                }
            }
            attachmentRepository.save(attachment);
            user.setAttachment(attachment);

            user.setRoles(roles);

            userRepository.save(user);
            return "redirect:/login";
        }

        return "redirect:/register";
    }
}
