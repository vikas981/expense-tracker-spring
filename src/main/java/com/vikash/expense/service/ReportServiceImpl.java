package com.vikash.expense.service;

import com.vikash.expense.entity.Expense;
import com.vikash.expense.entity.User;
import com.vikash.expense.enums.ReportType;
import com.vikash.expense.repository.ExpenseRepository;
import com.vikash.expense.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {


    private static final String REPORT_PATH = "/home/vikash/Downloads/expense/reports/pdf/";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Override
    public String generateReport(String type, Pageable page) throws FileNotFoundException, JRException {
         JasperPrint jasperPrint = getJasperPrint(page);
         String reportName = getReportName(REPORT_PATH,type);
        if(ReportType.CSV.name().equals(type)){

        } else if (ReportType.PDF.name().equals(type)) {
            log.info("Creating Pdf Report...........");
             JasperExportManager.exportReportToPdfFile(jasperPrint,reportName);
        } else if (ReportType.HTML.name().equals(type)) {
            log.info("Creating Html Report...........");
            JasperExportManager.exportReportToHtmlFile(jasperPrint,reportName);
        }
        return reportName;
    }

    private String getReportName(String path,String type){
        type = type.strip().toUpperCase();
        return new StringBuilder().append(path).append("EXPENSE_REPORT_").append(new Date(System.currentTimeMillis()))
                .append(".").append(type).toString();
    }

    public JasperPrint getJasperPrint(Pageable page) throws FileNotFoundException, JRException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        List<Expense> expenses = expenseRepository.findByUserId(user.getId(),page).toList();
        File file = ResourceUtils.getFile("classpath:expenses.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(expenses);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("Created By","Vikash Singh");
       return JasperFillManager.fillReport(jasperReport,parameters,dataSource);
    }
}
