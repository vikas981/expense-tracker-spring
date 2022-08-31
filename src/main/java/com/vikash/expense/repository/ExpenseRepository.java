package com.vikash.expense.repository;

import com.vikash.expense.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByCategory(String category, Pageable page);
    Page<Expense> findByUserId(Long userId,Pageable page);
    Page<Expense> findByName(String name, Pageable page);
    Page<Expense> findByDateBetween(Date startDate,Date endDate,Pageable page);
}
