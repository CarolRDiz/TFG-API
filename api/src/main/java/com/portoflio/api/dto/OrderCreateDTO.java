package com.portoflio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String secondAddress;
    private String city;
    private String postalCode;
    private String mobilePhone;
    private String status;
    private String totalPrice;

    //Cart ITEMS
    private List<CartItemDTO> cartItems;
}
