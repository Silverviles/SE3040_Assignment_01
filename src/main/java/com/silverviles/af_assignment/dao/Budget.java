package com.silverviles.af_assignment.dao;

import com.silverviles.af_assignment.common.BudgetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Budget {
    private String id;
    private String budgetName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BudgetType type;
    private Category category;
    private Double amount;
}
