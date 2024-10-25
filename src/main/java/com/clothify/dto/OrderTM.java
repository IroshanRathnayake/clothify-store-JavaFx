package com.clothify.dto;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderTM {
    private String id;
    private LocalDateTime date;
    private String customerId;
    private Double total;
    private JFXButton btnDelete;
}
