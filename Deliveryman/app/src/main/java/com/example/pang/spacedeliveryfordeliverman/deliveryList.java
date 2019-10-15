package com.example.pang.spacedeliveryfordeliverman;

public class deliveryList {
    private int deli_id;
    private int order_id;
    private String time;
    private String date;
    private String res_name;

    private int deliman_id;
    private String res_location;
    private String delivery_location;


    public deliveryList() {


    }

    public int getDeli_id() {
        return deli_id;
    }

    public void setDeli_id(int id) {
        this.deli_id = id;
    }

    public int getOder_id() {
        return order_id;
    }

    public void setOrder_id(int id) {
        this.order_id = id;
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

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }



    public long getDeliman_id() {
        return deliman_id;
    }

    public void setDeliman_id(int id) {
        this.deliman_id = id;
    }

    public String getRes_location() {
        return res_location;
    }

    public void setRes_location(String res_location) {
        this.res_location = res_location;
    }

    public String getDelivery_location() {
        return delivery_location;
    }

    public void setDelivery_location(String delivery_location) {
        this.delivery_location = delivery_location;
    }


}

