package com.vikash.expense.service;

import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;

public interface ReportService {
    String generateReport(String type, Pageable page) throws FileNotFoundException, JRException;
}
