package com.example.pang.spacedelivery;

public class InfoNotification {

    private int order_id;
    private int choose_id;
    private String time;
    private String date;

    private String foodName;
    private int Quantity;
    private String foodImg;

    public InfoNotification() {
    }

    public InfoNotification(String time, String date) {
        this.time = time;
        this.date = date;
    }
    public int getOrderId() {
        return order_id;
    }

    public void setOrderId(int id) {
        this.order_id = id;
    }

    public int getChooseId() {
        return choose_id;
    }

    public void setChooseId(int id) {
        this.choose_id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }
}
