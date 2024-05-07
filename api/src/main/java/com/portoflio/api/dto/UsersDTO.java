package com.portoflio.api.dto;

import com.portoflio.api.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Setter
@AllArgsConstructor
@NoArgsConstructor @Getter
public class UsersDTO implements Serializable {
    private Long id;
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
    private boolean admin;
    private List<Order> orders;

}