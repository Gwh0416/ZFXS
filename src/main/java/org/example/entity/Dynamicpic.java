package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 动态图片
 */
public class Dynamicpic {

    @TableId(value = "dynamicpicid")
    private Long dynamicpicid;//    图片id

    private Long dynamicid;//   所属动态id

    private int num;//  图片顺序

    private String url;//   图片url

    public Long getDynamicpicid() {
        return dynamicpicid;
    }

    public void setDynamicpicid(Long dynamicpicid) {
        this.dynamicpicid = dynamicpicid;
    }

    public Long getDynamicid() {
        return dynamicid;
    }

    public void setDynamicid(Long dynamicid) {
        this.dynamicid = dynamicid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
