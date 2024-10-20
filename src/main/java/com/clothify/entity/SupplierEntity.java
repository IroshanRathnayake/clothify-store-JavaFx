package com.clothify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity (name = "supplier")
public class SupplierEntity {
    @Id
    private String id;
    private String company;
    private String owner;
    private String address;
    private String phoneNumber;
    private String email;
}
