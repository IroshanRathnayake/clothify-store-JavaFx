package com.clothify.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {
    private String id;
    private String employeeID;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdAt;
}
