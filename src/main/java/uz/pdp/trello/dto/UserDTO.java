package uz.pdp.trello.dto;

import lombok.Value;

@Value
public class UserDTO {
    String email;
    String username;
    String password;
    String password2;
    byte[] photo;

}
