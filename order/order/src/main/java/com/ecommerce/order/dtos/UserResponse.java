package com.ecommerce.order.dtos;


import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private com.ecommerce.order.dtos.UserRole role;

    private AddressDto address;
}
