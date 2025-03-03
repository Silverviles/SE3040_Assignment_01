package com.silverviles.af_assignment.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.Income;
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
public class RecurringIncomeJob implements Job {
    private final MasterService masterService;
    private final ObjectMapper objectMapper;

    @Autowired
    public RecurringIncomeJob(MasterService masterService, ObjectMapper objectMapper) {
        this.masterService = masterService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String incomeJson = context.getMergedJobDataMap().getString("income");
        String username = context.getMergedJobDataMap().getString("username");

        Income income;
        try {
            income = objectMapper.readValue(incomeJson, Income.class);
            Date fireTime = context.getFireTime();
            LocalDate fireLocalDate = fireTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            income.setDate(fireLocalDate);
        } catch (Exception e) {
            log.error("Error deserializing income object", e);
            throw new JobExecutionException(e);
        }

        try {
            log.info("Adding recurring income: {}", income);
            masterService.addIncome(username, income);
            masterService.sendEmail(
                    username,
                    "Recurring income added",
                    "Recurring income of " + income.getAmount() + " added to your account"
            );
            log.info("Recurring income added successfully");
        } catch (ServiceException e) {
            log.error("Error adding recurring income", e);
            throw new JobExecutionException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
