package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;

public class Image {
    @TableId("imageid")
    private Long imageid;
    private String url;

    public Long getImageid() {
        return imageid;
    }

    public void setImageid(Long imageid) {
        this.imageid = imageid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
