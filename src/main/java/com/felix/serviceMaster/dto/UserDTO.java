package com.felix.serviceMaster.dto;

import com.felix.serviceMaster.enums.UserRole;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;

    private String email;

    private String password;

    private String name;

    private String lastname;

    private String phone;

    private UserRole role;
}
