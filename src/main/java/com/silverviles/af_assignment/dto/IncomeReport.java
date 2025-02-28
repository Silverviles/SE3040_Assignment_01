package com.silverviles.af_assignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.silverviles.af_assignment.dao.Income;
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
public class IncomeReport implements Serializable {
    private String name;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("end_date")
    private LocalDate endDate;
    private List<Income> incomes;
    @JsonProperty("total_income")
    private double totalIncome;
}
