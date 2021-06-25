package com.example.digitalshop.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Product
{
    private  String id;
    private  String uid;
    private String name;
    private String detail;

    public String getPrice () {
        return price;
    }

    public void setPrice (String price) {
        this.price = price;
    }

    public String getCategory () {
        return category;
    }

    public void setCategory (String category) {
        this.category = category;
    }

    private String price;
    private String category;

    public Double getRating () {
        return rating;
    }

    public void setRating (Double rating) {
        this.rating = rating;
    }

    private  Double rating;
    private  List<String> images;
    private String uploadername;
    private  String uploadershopname;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getUid () {
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

    public String getDetail () {
        return detail;
    }

    public void setDetail (String detail) {
        this.detail = detail;
    }

    public List<String> getImages () {
        return images;
    }

    public void setImages (List<String> image) {
        this.images=image;
    }
    public void setArrImages (String[] image) {
        this.images=new ArrayList<>();
        this.images.addAll(Arrays.asList(image));
    }

    public String getUploadername () {
        return uploadername;
    }

    public void setUploadername (String uploadername) {
        this.uploadername = uploadername;
    }

    public String getUploadershopname () {
        return uploadershopname;
    }

    public void setUploadershopname (String uploadershopname) {
        this.uploadershopname = uploadershopname;
    }

    public String getAddress () {
        return address;
    }

    public void setAddress (String address) {
        this.address = address;
    }

    private  String address;

}