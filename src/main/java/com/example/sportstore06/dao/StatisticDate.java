package com.example.sportstore06.dao;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class StatisticDate {
    private String startDate;
    private String endDate;

    public StatisticDate() {
    }

    public StatisticDate(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Timestamp getStartDate() {
        if (startDate != null) {
            LocalDate localDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
            return Timestamp.valueOf(localDateTime);
        }
        return new Timestamp(new Date().getTime());
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        if (startDate != null) {
            LocalDate localDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
            return Timestamp.valueOf(localDateTime);
        }
        return new Timestamp(new Date().getTime());
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
