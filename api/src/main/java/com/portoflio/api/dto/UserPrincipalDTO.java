package com.portoflio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Setter
@AllArgsConstructor
@NoArgsConstructor @Getter
public class UserPrincipalDTO implements Serializable {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String secondAddress;
    private String city;
    private String postalCode;
    private String phone;
    private LocalDateTime created;
    //private Set<Cart> carts;
    private boolean admin;

}