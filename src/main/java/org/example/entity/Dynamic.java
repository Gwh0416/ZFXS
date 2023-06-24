package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;


/**
 * 动态
 */
public class Dynamic {

    @TableId(value = "dynamicid")
    private Long dynamicid;//       动态id

    private Long userid;//      动态所属用户id

    private String content;//       动态文字内容

    private LocalDateTime time;//   动态发布时间

    private Integer isdelete;

    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }
}
