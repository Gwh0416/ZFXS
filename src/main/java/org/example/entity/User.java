package org.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * 用户
 */
public class User {

    @TableId(value = "userid")
    private Long userid;//  用户id

    private String phone;//     手机号

    private String password;//      密码

    private String username;//      用户名

    private String url;//      用户头像url

    private LocalDateTime time;

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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private Integer state;//        用户状态（0禁用，1启用）



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
