package com.silverviles.af_assignment.dto;

import com.silverviles.af_assignment.dao.Category;
import com.silverviles.af_assignment.dao.Expense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddExpenseRequest {
    private String name;
    private double amount;
    private LocalDate date;
    private Category category;
    private List<String> tags;
    private String description;
    private Boolean recurring;
    private Integer recurringIntervalInHours;
    private String recurringEndDate;

    public Expense getExpense() {
        Expense expense = new Expense();
        expense.setName(name);
        expense.setAmount(amount);
        expense.setDate(date);
        expense.setCategory(category);
        expense.setTags(tags);
        expense.setDescription(description);
        return expense;
    }
}
