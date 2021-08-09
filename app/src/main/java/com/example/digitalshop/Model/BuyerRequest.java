package com.example.digitalshop.Model;

public class BuyerRequest
{


    public String getUser_uid () {
        return user_uid;
    }

    public void setUser_uid (String user_uid) {
        this.user_uid = user_uid;
    }

    public String getSeller_uid () {
        return seller_uid;
    }

    public void setSeller_uid (String seller_uid) {
        this.seller_uid = seller_uid;
    }

    public String getUser_name () {
        return user_name;
    }

    public void setUser_name (String user_name) {
        this.user_name = user_name;
    }

    public String getRequest_discription () {
        return request_discription;
    }

    public void setRequest_discription (String request_discription) {
        this.request_discription = request_discription;
    }

    public String getUser_phone () {
        return user_phone;
    }

    public void setUser_phone (String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email () {
        return user_email;
    }

    public void setUser_email (String user_email) {
        this.user_email = user_email;
    }

    public String getKey () {
        return key;
    }

    public void setKey (String key) {
        this.key = key;
    }

    private String key;

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    private String title;
    private  String user_uid;
    private  String seller_uid;
    private  String user_name;
    private String request_discription;
    private  String user_phone;
    private  String user_email;



}
