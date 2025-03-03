package com.silverviles.af_assignment.dto;

import com.silverviles.af_assignment.dao.Income;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddIncomeRequest {
    private String name;
    private double amount;
    private LocalDate date;
    private List<String> tags;
    private String description;
    private Boolean recurring;
    private Integer recurringIntervalInHours;
    private String recurringEndDate;

    public Income getIncome() {
        Income income = new Income();
        income.setName(name);
        income.setAmount(amount);
        income.setDate(date);
        income.setTags(tags);
        income.setDescription(description);
        return income;
    }
}
