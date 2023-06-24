package org.example.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

/**
 * 账本
 */
public class Diary {

    @TableId(value = "diaryid")
    private Long diaryid;//     账本id

    private String content;//   账本描述

    private LocalDateTime time;//   时间

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    private Long userid;//    创建用户id

    public void setDiaryid(Long diaryid) {
        this.diaryid = diaryid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getUserid1() {
        return userid1;
    }

    public void setUserid1(Long userid1) {
        this.userid1 = userid1;
    }

    public Long getUserid2() {
        return userid2;
    }

    public void setUserid2(Long userid2) {
        this.userid2 = userid2;
    }

    private Long userid1;//     共享用户id1

    private Long userid2;//     共享用户id2

    private Integer isdelete;

    private float income;
    private float outcome;
    private int incomenum;
    private int outcomenum;

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

    public int getIncomenum() {
        return incomenum;
    }

    public void setIncomenum(int incomenum) {
        this.incomenum = incomenum;
    }

    public int getOutcomenum() {
        return outcomenum;
    }

    public void setOutcomenum(int outcomenum) {
        this.outcomenum = outcomenum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Long getDiaryid() {
        return diaryid;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }
}
