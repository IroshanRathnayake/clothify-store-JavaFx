package com.clothify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String id;
    private String title;
    private String name;
    private String position;
    private String address;
    private String phoneNumber;
    private String email;
    public Boolean loginAccess;
}
