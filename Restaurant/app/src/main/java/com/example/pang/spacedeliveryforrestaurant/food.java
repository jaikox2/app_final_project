package com.example.pang.spacedeliveryforrestaurant;

public class food {
    private int id;
    private String name;
    private String Detail;
    private double Price;
    private double Stamp;
    private String image;
    private String baseUrl;

    public food() {
    }

    public food(String name, String detail, double price,double stamp ,String image) {
        this.name = name;
        this.Detail = detail;
        this.Price = price;
        this.Stamp = stamp;
        this.image = image;
    }
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        this.Detail = detail;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    public double getStamp() {
        return Stamp;
    }

    public void setStamp(double stamp) {
        this.Stamp = stamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

