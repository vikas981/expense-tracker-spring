package com.vikash.expense.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class UserRequest {
    private String email;
    private String password;
}
