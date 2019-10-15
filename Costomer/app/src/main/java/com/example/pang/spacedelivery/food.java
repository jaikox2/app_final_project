package com.example.pang.spacedelivery;

public class food {
    private int Foodid;
    private String Foodname;
    private String FoodDetail;
    private double FoodPrice;
    private double FoodStamp;
    private String Foodimage;

    private int Resid;
    private String Resname;
    private String ResAddress;
    private String Resimage;
    private String baseUrl;

    private boolean addedTocart = false;
    private String checkChoose;
    private int cart_amount;
    private int cart_id;



    public food() {
    }

    public food(String name,double price,String image,String resname,String resAddress,String resimage) {
        this.Foodname = name;
        this.FoodPrice = price;
        this.Foodimage = image;
        this.Resname = resname;
        this.ResAddress = resAddress;
        this.Resimage = resimage;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getCartAmount() {
        return cart_amount;
    }

    public void setCartAmount(int cartAmount) {
        this.cart_amount = cartAmount;
    }

    public String getCheckChoose() {
        return checkChoose;
    }

    public void setCheckChoose(String checkChoose1) {
        this.checkChoose = checkChoose1;
    }

    public boolean isAddedTocart() {
        return addedTocart;
    }

    public void setAddedTocart(boolean addedTocart) {
        this.addedTocart = addedTocart;
    }

    public long getFoodId() {
        return Foodid;
    }

    public void setFoodId(int id) {
        this.Foodid = id;
    }

    public String getFoodName() {
        return Foodname;
    }

    public void setFoodName(String name) {
        this.Foodname = name;
    }

    public String getFoodDetail() {
        return FoodDetail;
    }

    public void setFoodDetail(String detail) {
        this.FoodDetail = detail;
    }

    public double getFoodPrice() { return FoodPrice; }

    public void setFoodPrice(double price) {
        this.FoodPrice = price;
    }

    public double getFoodStamp() {
        return FoodStamp;
    }

    public void setFoodStamp(double stamp) {
        this.FoodStamp = stamp;
    }

    public String getFoodImage() { return Foodimage; }

    public void setFoodImage(String image) {
        this.Foodimage = image;
    }

    public long getResId() {
        return Resid;
    }

    public void setResId(int id) {
        this.Resid = id;
    }

    public String getResName() {
        return Resname;
    }

    public void setResName(String name) {
        this.Resname = name;
    }

    public String getResAddress() {
        return ResAddress;
    }

    public void setResAddress(String address) {
        this.ResAddress = address;
    }

    public String getResImage() { return Resimage; }

    public void setResImage(String image) {
        this.Resimage = image;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

