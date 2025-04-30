package uz.pdp.trello.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDTO {
    private String name;
    private String username;
    private String password;
    private String password2;
}
