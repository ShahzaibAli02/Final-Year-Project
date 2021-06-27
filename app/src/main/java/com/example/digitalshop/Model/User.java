package com.example.digitalshop.Model;

import java.io.Serializable;

public class User implements Serializable
{

    public  String uid;
    public  String name;
    public  String email;
    public  String phone;
    public  String role;

    public String getShopname () {
        return shopname;
    }

    public void setShopname (String shopname) {
        this.shopname = shopname;
    }

    public  String shopname;

    public String getRole () {
        return role;
    }

    public void setRole (String role) {
        this.role = role;
    }

    public String getImage () {
        return image;
    }

    public void setImage (String image) {
        this.image = image;
    }

    public  String image;

    public String getUid ()
    {
        return uid;
    }

    public void setUid (String uid) {
        this.uid = uid;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getPhone () {
        return phone;
    }

    public void setPhone (String phone) {
        this.phone = phone;
    }

    public String getAddress () {
        return address;
    }

    public void setAddress (String address) {
        this.address = address;
    }

    public  String address;



}
