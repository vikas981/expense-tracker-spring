package com.vikash.expense.service;

import com.vikash.expense.dto.request.ExpenseRequest;
import com.vikash.expense.entity.Expense;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public interface ExpenseService {
    List<Expense> getAllExpenses(Pageable page);
    List<Expense> readByCategory(String category, Pageable page);
    List<Expense> readByName(String category, Pageable page);
    void createExpense(ExpenseRequest expenseRequest);
    void updateExpense(ExpenseRequest expenseRequest);
    void  deleteExpense(Long expenseId);

    List<Expense> readByDate(Date startDate, Date endDate, Pageable page);


}
