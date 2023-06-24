package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 点赞
 */
public class Likes {

    @TableId(value = "likepageid")
    private Long likepageid;//  点赞id

    private Long dynamicid;//   动态id

    private Long userid;//  用户id

    public Long getLikepageid() {
        return likepageid;
    }

    public void setLikepageid(Long likepageid) {
        this.likepageid = likepageid;
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
}
