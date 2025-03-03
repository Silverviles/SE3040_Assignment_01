package com.silverviles.af_assignment.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.Expense;
import com.silverviles.af_assignment.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class RecurringExpenseJob implements Job {
    private final MasterService masterService;
    private final ObjectMapper objectMapper;

    @Autowired
    RecurringExpenseJob(MasterService masterService, ObjectMapper objectMapper) {
        this.masterService = masterService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String expenseJson = context.getMergedJobDataMap().getString("expense");
        String username = context.getMergedJobDataMap().getString("username");

        Expense expense;
        try {
            expense = objectMapper.readValue(expenseJson, Expense.class);
            Date fireTime = context.getFireTime();
            LocalDate fireLocalDate = fireTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            expense.setDate(fireLocalDate);
        } catch (Exception e) {
            log.error("Error deserializing expense object", e);
            throw new JobExecutionException(e);
        }

        try {
            log.info("Adding recurring expense: {}", expense);
            masterService.addExpense(username, expense);
            masterService.sendEmail(
                    username,
                    "Recurring expense added",
                    "Recurring expense added: " + expense.getAmount()
            );
            log.info("Recurring expense added successfully");
        } catch (ServiceException | IOException e) {
            log.error("Error adding recurring expense", e);
            throw new JobExecutionException(e);
        }
    }
}
