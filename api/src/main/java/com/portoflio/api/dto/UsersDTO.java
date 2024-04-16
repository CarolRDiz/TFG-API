package com.portoflio.api.dto;

import com.portoflio.api.models.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


@Setter
@AllArgsConstructor
@NoArgsConstructor @Getter
public class UsersDTO implements Serializable {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;

    public UsersDTO(Users users) {
        this.id         = users.getId();
        this.name       = users.getName();
        this.surname    = users.getSurname();
        this.email      = users.getEmail();
    }

}