package com.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditResult {
    private int score;
    private char creditGrade;
    private long userId;
    private String loanLimit;
}
