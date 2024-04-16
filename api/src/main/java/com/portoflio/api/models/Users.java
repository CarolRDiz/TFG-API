package com.portoflio.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class Users{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private LocalDateTime created;
    private boolean admin;


    public Users(String username, String name, String surname, String email, String password, boolean admin) {
        this.username = username;
        this.name       = name;
        this.surname    = surname;
        this.email      = email;
        this.password   = password;
        this.admin = admin;
    }

    public Users(Long id, String username, String surname, String password, boolean admin) {
        this.id = id;
        this.username = username;
        this.surname    = surname;
        this.password = password;
        this.admin = admin;
    }
    /*
    public Users(RegistrationDTO newUsers) {
        this.name       = newUsers.getName();
        this.surname    = newUsers.getSurname();
        this.email      = newUsers.getEmail();
        this.password   = newUsers.getPassword();
    }

 */
}