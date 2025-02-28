package com.silverviles.af_assignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.silverviles.af_assignment.dao.Category;
import com.silverviles.af_assignment.dao.Expense;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseReport implements Serializable {
    private String name;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("end_date")
    private LocalDate endDate;
    private Category category;
    private List<Expense> expenses;
    @JsonProperty("total_expenses")
    private double totalExpenses;
}
