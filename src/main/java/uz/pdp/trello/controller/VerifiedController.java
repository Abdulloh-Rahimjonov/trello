package uz.pdp.trello.controller;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
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
import uz.pdp.trello.repo.UserRepository;
import uz.pdp.trello.service.EmailService;

import javax.xml.transform.sax.SAXResult;
import java.util.ArrayList;
import java.util.List;
@MultipartConfig
@Controller
@RequiredArgsConstructor
public class VerifiedController {

    private final EmailService emailService;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;

    @PostMapping("/verifiedPage")
    public String getVerified(@ModelAttribute UserDTO userDTO, Model model) {
        String randomCode = emailService.randomCode();
        emailService.sendEmail(userDTO.getEmail() , randomCode);
        model.addAttribute("randomCode", randomCode);
        model.addAttribute("userDTO", userDTO);
        return "redirect:/verified";
    }

    @PostMapping("/verifiedd")
    public String verified(@RequestParam String randomCode, @ModelAttribute UserDTO userDTO , @RequestParam String userCode , Model model) {
        if (!randomCode.equals(userCode)) {
            model.addAttribute("userDTO", userDTO);
            return "redirect:/verifiedPage";
        }
        if (userDTO.getPassword().equals(userDTO.getPassword2())) {
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            List<Role> roles = new ArrayList<>();
            roles.add(new Role(null, Roles.PROGRAMMER));
            user.setRoles(roles);
            user.setUsername(userDTO.getUsername());
            Attachment attachment = new Attachment();
            attachment.setContent(userDTO.getPhoto());
            attachmentRepository.save(attachment);
            user.setAttachment(attachment);
            userRepository.save(user);
            return "redirect:/login";
        }
        return "redirect:/register";


    }



}
