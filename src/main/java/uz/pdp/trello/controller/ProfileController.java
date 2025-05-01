package uz.pdp.trello.controller;

import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.trello.entity.Attachment;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.repo.AttachmentRepository;
import uz.pdp.trello.repo.UserRepository;

import java.io.IOException;
import java.util.Optional;

@MultipartConfig
@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    public ProfileController(UserRepository userRepository, AttachmentRepository attachmentRepository) {
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @GetMapping("")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "profile";
    }


    @PostMapping(path = "/updatePage")
    public String updateProfilePage(@RequestParam("userId") Integer userId, Model model) {
        User user = userRepository.findById(userId).orElseThrow();
        model.addAttribute("user", user);
        return "updateUserPage";
    }

    @Transactional
    @PostMapping("/update")
    public String updateProfile(@RequestParam("file") MultipartFile file,
                                @RequestParam("userId") Integer id,
                                @RequestParam("name") String name,
                                @RequestParam("username") String username , Model model) throws IOException {
        User user = userRepository.findById(id).orElseThrow();

        user.setName(name);
        user.setUsername(username);

        if (!file.isEmpty()) {
            Attachment attachment;


            if (user.getAttachment() != null && user.getAttachment().getId() != null) {
                attachment = attachmentRepository.findById(user.getAttachment().getId()).orElse(new Attachment());
            } else {
                attachment = new Attachment();
            }

            attachment.setContent(file.getBytes());
            attachmentRepository.save(attachment);

            user.setAttachment(attachment);
        }

        userRepository.save(user);
        model.addAttribute("user", user);
        return "profile";
    }


}
