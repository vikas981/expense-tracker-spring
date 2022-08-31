package com.vikash.expense.entity;

import lombok.Data;

@Data
public class Task {
    private String cronExpression;
    private String actionType;
    private String data;
}
