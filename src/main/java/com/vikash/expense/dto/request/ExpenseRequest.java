package com.vikash.expense.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Builder
public class ExpenseRequest {

    private Long id;
    private String name;
    private BigDecimal amount;
    private String category;
    private String description;
    private Date date;
}
