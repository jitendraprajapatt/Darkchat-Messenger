package com.app.darkchat;

public class search_class {
    String username ,imgUrl ,key ,status;

    public search_class() {

    }

    public search_class(String username, String imgUrl, String key, String status) {
        this.username = username;
        this.imgUrl = imgUrl;
        this.key = key;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
