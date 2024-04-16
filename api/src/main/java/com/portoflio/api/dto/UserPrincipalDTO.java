package com.portoflio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Setter
@AllArgsConstructor
@NoArgsConstructor @Getter
public class UserPrincipalDTO implements Serializable {
    private String username;
    private String name;
    private String surname;
    private String email;
    //private Set<Cart> carts;
    private boolean admin;
    //private Set<Course> createdCourses = new HashSet<>();

}