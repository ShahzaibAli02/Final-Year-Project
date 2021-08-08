package com.example.digitalshop.Model;

import java.io.Serializable;

public class User implements Serializable
{

    public  String uid;
    public  String name;

    public String getShop_name () {
        return shop_name;
    }

    public void setShop_name (String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_description () {
        return shop_description;
    }

    public void setShop_description (String shop_description) {
        this.shop_description = shop_description;
    }

    public double getLat () {
        return lat;
    }

    public void setLat (double lat) {
        this.lat = lat;
    }

    public double getLng () {
        return lng;
    }

    public void setLng (double lng) {
        this.lng = lng;
    }

    public  String shop_name;
    public  String shop_description;

    public  double lat;
    public  double lng;


    public  String email;
    public  String phone;
    public  String role;


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

    @Override
    public String toString () {
        return shop_name;
    }
}
