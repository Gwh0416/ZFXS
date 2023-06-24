package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 账单类型
 */
public class Kind {

    @TableId(value = "kindid")
    private Long kindid;//  账单类型id

    private String content;//   账单描述

    private String url;//   账单图片url

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getKindid() {
        return kindid;
    }

    public void setKindid(Long kindid) {
        this.kindid = kindid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
