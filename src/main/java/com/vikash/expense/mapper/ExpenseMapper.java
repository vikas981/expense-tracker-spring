package com.vikash.expense.mapper;

import com.vikash.expense.dto.request.ExpenseRequest;
import com.vikash.expense.entity.Expense;
import org.springframework.stereotype.Component;

import java.sql.Date;


public interface ExpenseMapper {

    static Expense dtoToEntity(ExpenseRequest request){
        return  Expense.builder().
                name(request.getName()).
                amount(request.getAmount()).
                category(request.getCategory()).
                description(request.getDescription()).
                date(request.getDate() == null? new Date(System.currentTimeMillis()) :request.getDate()).
                build();
    }
}
