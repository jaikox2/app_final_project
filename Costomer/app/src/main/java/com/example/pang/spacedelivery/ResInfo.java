package com.example.pang.spacedelivery;

public class ResInfo {

    private int id;
    private String Resname;
    private String Resaddress;
    private String Resphone;
    private String image;
    private String baseUrl;

    public ResInfo() {
    }

    public ResInfo(String name, String address,String resphone ,String image) {
        this.Resname = name;
        this.Resaddress = address;
        this.Resphone = resphone;
        this.image = image;
    }
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResName() {
        return Resname;
    }

    public void setResName(String name) {
        this.Resname = name;
    }

    public String getResaddress() {
        return Resaddress;
    }

    public void setResaddress(String resaddress) {
        this.Resaddress = resaddress;
    }

    public String getResphone() {
        return Resphone;
    }

    public void setResphone(String resphone) {
        this.Resphone = resphone;
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
