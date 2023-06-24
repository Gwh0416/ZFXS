package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

public class Message {
    @TableId("messageid")
    private Long messageid;
    private String content;
    private LocalDateTime time;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getMessageid() {
        return messageid;
    }

    public void setMessageid(Long messageid) {
        this.messageid = messageid;
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
}
