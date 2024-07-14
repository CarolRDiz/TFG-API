package com.portoflio.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @NotNull(message = "Email cannot be null")
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String secondAddress;
    private String city;
    private String postalCode;
    private String phone;
    private String password;
    private LocalDateTime created;
    private boolean admin;

    public Users(String email, String password, boolean admin ) {
        this.admin = admin;
        this.password = password;
        this.email = email;
    }

    public Users(String email, String firstName, String lastName, String address, String secondAddress, String city, String postalCode, String phone, String password, boolean admin) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.secondAddress = secondAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.password = password;
        this.admin = admin;
    }

    //TODO : CascadeType?
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<Order>();
}