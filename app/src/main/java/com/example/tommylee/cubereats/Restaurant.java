package com.example.tommylee.cubereats;

public class Restaurant {

    /**
     * imgurl :
     * name : Med Can
     * time : 07:00-22:00
     */

    private String imgurl;
    private String name;
    private String time;
    private String id;

    public void setId(String id){
        this.id=id;
    }
    public String getId() {
        return id;
    }
    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
