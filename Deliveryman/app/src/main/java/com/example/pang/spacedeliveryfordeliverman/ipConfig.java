package com.example.pang.spacedeliveryfordeliverman;

public class ipConfig {
    //public String baseUrl= "http://192.168.137.1/testPJ/";
    public String baseUrl= "http://10.0.2.2/testPJ/";

    public ipConfig() {
    }

    public ipConfig(String url) {
        this.baseUrl = url;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getBaseUrlFood() {
        String foodUrl = baseUrl+"FoodDB/";
        return foodUrl;
    }


    public String getBaseUrlRes() {
        return baseUrl+"ResTable/";
    }

    public String getBaseUrlOrder() {
        return baseUrl+"orderTable/";
    }

    public String getBaseUrlDeliman() {
        return baseUrl+"DelimanTable/";
    }

    public String getBaseUrlDelivery() {
        return baseUrl+"DeliveryTable/";
    }

    public String getBaseUrlCustomer() {
        return baseUrl+"Customer/";
    }
}
