package com.example.sportstore06.dao;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StatisticDate {
    private Timestamp startDate;
    private Timestamp endDate;
}
