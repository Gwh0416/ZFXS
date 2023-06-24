package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

/**
 * 收藏
 */
public class Collect {

    @TableId(value = "collectid")
    private Long collectid;//   收藏id

    private Long dynamicid;//   动态id

    private Long userid;//  用户id

    private LocalDateTime collecttime;//    收藏时间

    private Integer isdelete;

    public Long getCollectid() {
        return collectid;
    }

    public void setCollectid(Long collectid) {
        this.collectid = collectid;
    }

    public Long getDynamicid() {
        return dynamicid;
    }

    public void setDynamicid(Long dynamicid) {
        this.dynamicid = dynamicid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public LocalDateTime getCollecttime() {
        return collecttime;
    }

    public void setCollecttime(LocalDateTime collecttime) {
        this.collecttime = collecttime;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }
}
