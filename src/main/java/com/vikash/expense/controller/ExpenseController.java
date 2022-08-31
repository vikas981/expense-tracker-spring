package com.vikash.expense.controller;

import com.vikash.expense.dto.request.ExpenseRequest;
import com.vikash.expense.entity.Expense;
import com.vikash.expense.service.ExpenseService;
import com.vikash.expense.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

@RestController
public class ExpenseController {

    private final ExpenseService expenseService;

    private final ReportService reportService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    public ExpenseController(ExpenseService expenseService, ReportService reportService) {
        this.expenseService = expenseService;
        this.reportService = reportService;
    }

    @GetMapping("/expenses")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Expense>> getAllExpenses(Pageable page) {
        return new ResponseEntity<>(expenseService.getAllExpenses(page), HttpStatus.OK);
    }

    @GetMapping("/expenses/reports")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> generateReport(@RequestParam(value = "type") String type, Pageable page) throws JRException, IOException {
        String reportName = reportService.generateReport(type, page);
        File file = new File(reportName);
        if (file.exists()) {
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/pdf";
            }
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

        @GetMapping("expenses/category")
        public ResponseEntity<List<Expense>> getExpensesByCategory
        (@RequestParam(value = "category", required = true) String category, Pageable page){
            return new ResponseEntity<>(expenseService.readByCategory(category, page), HttpStatus.OK);
        }

        @GetMapping("expenses/name")
        public ResponseEntity<List<Expense>> getExpensesByName (@RequestParam(value = "name", required = true) String
        name, Pageable page){
            return new ResponseEntity<>(expenseService.readByName(name, page), HttpStatus.OK);
        }

        @GetMapping("expenses/date")
        public ResponseEntity<List<Expense>> getAllExpensesByDate
        (@RequestParam(value = "startDate", required = false) Date startDate,
                @RequestParam(value = "endDate", required = false) Date endDate,
                Pageable page){
            return new ResponseEntity<>(expenseService.readByDate(startDate, endDate, page), HttpStatus.OK);
        }

        @PostMapping("/expenses")
        public ResponseEntity<String> saveExpense (@Valid @RequestBody ExpenseRequest request){
            expenseService.createExpense(request);
            return new ResponseEntity<>("Expense saved successfully!", HttpStatus.CREATED);
        }

        @PutMapping("/expenses")
        public ResponseEntity<String> updateExpense (@Valid @RequestBody ExpenseRequest request){
            expenseService.updateExpense(request);
            return new ResponseEntity<>("Expense updated successfully!", HttpStatus.NO_CONTENT);
        }

        @DeleteMapping("/expenses/{id}")
        public ResponseEntity<String> deleteExpense (@PathVariable(value = "id") Long id){
            expenseService.deleteExpense(id);
            return new ResponseEntity<>("Expense deleted successfully!", HttpStatus.OK);
        }
    }
