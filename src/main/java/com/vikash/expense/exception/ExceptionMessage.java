package com.vikash.expense.exception;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExceptionMessage {
    private List<String> message;
    private Integer status;
    private String timestamp;
}
