package no3.game.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class MemberJoinDto {

    private String name;
    private String email;
    private String password;
    private String address;
    private boolean social;
}