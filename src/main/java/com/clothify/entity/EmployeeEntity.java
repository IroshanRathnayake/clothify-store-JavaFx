package com.clothify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "employee")
public class EmployeeEntity {
    @Id
    private String id;
    private String title;
    private String name;
    private String position;
    private String address;
    private String phoneNumber;
    private String email;
    public Boolean loginAccess;
}