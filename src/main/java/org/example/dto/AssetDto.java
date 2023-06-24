package org.example.dto;

import java.time.LocalDateTime;

public class AssetDto {

    private long userid;
    private LocalDateTime starttime;
    private LocalDateTime endtime;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }

    public LocalDateTime getEndtime() {
        return endtime;
    }

    public void setEndtime(LocalDateTime endtime) {
        this.endtime = endtime;
    }

    private float income;
    private float outcome;
    private int incomeNum;
    private int outcomeNum;

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getOutcome() {
        return outcome;
    }

    public void setOutcome(float outcome) {
        this.outcome = outcome;
    }

    public int getIncomeNum() {
        return incomeNum;
    }

    public void setIncomeNum(int incomeNum) {
        this.incomeNum = incomeNum;
    }

    public int getOutcomeNum() {
        return outcomeNum;
    }

    public void setOutcomeNum(int outcomeNum) {
        this.outcomeNum = outcomeNum;
    }
}
