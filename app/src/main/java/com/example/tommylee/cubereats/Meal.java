package com.example.tommylee.cubereats;


public class Meal {

    private String imgurl;
    private String name;
    private double price;
    private String id;
    private String description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = (double)price;
    }

}

