package com.vikash.expense.service;

import com.vikash.expense.dto.request.ExpenseRequest;
import com.vikash.expense.entity.Expense;
import com.vikash.expense.entity.User;
import com.vikash.expense.enums.ReportType;
import com.vikash.expense.exception.ExpenseNotFoundException;
import com.vikash.expense.mapper.ExpenseMapper;
import com.vikash.expense.repository.ExpenseRepository;
import com.vikash.expense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;


    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserService userService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    @Override
    public List<Expense> getAllExpenses(Pageable page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        return expenseRepository.findByUserId(user.getId(),page).toList();
    }

    @Override
    public List<Expense> readByCategory(String category, Pageable page) {
        return expenseRepository.findByCategory(category,page).toList();
    }

    @Override
    public List<Expense> readByName(String category, Pageable page) {
        return expenseRepository.findByName(category,page).toList();
    }

    @Override
    @Transactional
    public void createExpense(ExpenseRequest expenseRequest) {
        Expense expense = ExpenseMapper.dtoToEntity(expenseRequest);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =userRepository.findByEmail(authentication.getName()).orElse(null);
        expense.setUser(user);
        expenseRepository.save(expense);
    }

    @Override
    public void updateExpense(ExpenseRequest expenseRequest) {

        Expense expense = expenseRepository.findById(expenseRequest.getId()).
                orElseThrow(() -> new ExpenseNotFoundException("expense not exists for id: " + expenseRequest.getId()));
        expense.setName(expenseRequest.getName() == null ? expense.getName() : expenseRequest.getName());
        expense.setAmount(expenseRequest.getAmount() == null ? expense.getAmount() : expenseRequest.getAmount());
        expense.setCategory(expenseRequest.getCategory() == null ? expense.getCategory() : expenseRequest.getCategory());
        expense.setDescription(expenseRequest.getDescription() == null ? expense.getDescription() : expenseRequest.getDescription());
        expense.setDate(expenseRequest.getDate() == null ? expense.getDate() : expenseRequest.getDate());
        expenseRepository.save(expense);


    }

    @Override
    public void deleteExpense(Long expenseId) {
        try{
            expenseRepository.deleteById(expenseId);
        }catch (Exception e){
            throw new ExpenseNotFoundException("expense not exists for id: " + expenseId);
        }
    }

    @Override
    public List<Expense> readByDate(Date startDate, Date endDate, Pageable page) {
        if(Objects.isNull(startDate)){
            startDate = new Date(0);
        }
        if(Objects.isNull(endDate)){
            endDate = new Date(System.currentTimeMillis());
        }
        Page<Expense> pages = expenseRepository.findByDateBetween(startDate,endDate,page);
        return pages.toList();
    }


}
