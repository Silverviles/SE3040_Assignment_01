package com.silverviles.af_assignment.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Budget {
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double amount;
}
