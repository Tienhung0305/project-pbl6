package com.example.sportstore06.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class StatisticDate {
    private String startDate;
    private String endDate;

    public StatisticDate(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Timestamp getStartDate() {
        LocalDate localDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        return Timestamp.valueOf(localDateTime);
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        LocalDate localDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        return Timestamp.valueOf(localDateTime);
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
