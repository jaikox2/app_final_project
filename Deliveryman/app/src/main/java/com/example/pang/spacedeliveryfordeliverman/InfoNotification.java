package com.example.pang.spacedeliveryfordeliverman;

public class InfoNotification {

    private int order_id;
    private int choose_id;
    private String time;
    private String date;

    public InfoNotification() {
    }

    public InfoNotification(String time, String date) {
        this.time = time;
        this.date = date;
    }
    public long getOrderId() {
        return order_id;
    }

    public void setOrderId(int id) {
        this.order_id = id;
    }

    public long getChooseId() {
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
}
