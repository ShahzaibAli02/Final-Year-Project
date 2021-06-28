package com.example.digitalshop.Model;

import com.example.digitalshop.Enums.OrderStatus;
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Serializable
{

    public String getOrderid () {
        return orderid;
    }

    public void setOrderid (String orderid) {
        this.orderid = orderid;
    }

    public Long getCreatedat () {
        return createdat;
    }

    public void setCreatedat (Long createdat) {
        this.createdat = createdat;
    }

    public  String  getFormatedDate()
    {
       return new SimpleDateFormat("MM/dd/yyyy").format(new Date(getCreatedat() * 1000L));
    }
    public  String  getFormatedTime()
    {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(getCreatedat() * 1000L));
    }
    public  String  getFormatedDay()
    {
        return new SimpleDateFormat("dd").format(new Date(getCreatedat() * 1000L));
    }

    public Long getUpdatedat () {
        return updatedat;
    }

    public void setUpdatedat (Long updatedat) {
        this.updatedat = updatedat;
    }

    public OrderStatus getOrderStatus () {
        return orderStatus;
    }

    public void setOrderStatus (OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    public User getUser () {
        return user;
    }

    public void setUser (User user) {
        this.user = user;
    }

    public Product getProduct () {
        return product;
    }

    public void setProduct (Product product) {
        this.product = product;
    }

    private  String orderid;
    private  String userid;

    public Boolean getIsreviewed () {
        return isreviewed;
    }

    public void setIsreviewed (Boolean isreviewed) {
        this.isreviewed = isreviewed;
    }

    private  Boolean isreviewed=false;

    public String getUserid () {
        return userid;
    }

    public void setUserid (String userid) {
        this.userid = userid;
    }

    public String getSellerid () {
        return sellerid;
    }

    public void setSellerid (String sellerid) {
        this.sellerid = sellerid;
    }

    private  String sellerid;
    private  Long createdat;
    private  Long updatedat;
    private  OrderStatus orderStatus;
    private  User user;
    private  Product product;
    private  Integer quantity;




    public String getPayment_method () {
        return payment_method;
    }

    public void setPayment_method (String payment_method) {
        this.payment_method = payment_method;
    }

    private  String payment_method;

    public Integer getQuantity () {
        return quantity;
    }

    public void setQuantity (Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalprice () {
        return totalprice;
    }

    public void setTotalprice (Double totalprice) {
        this.totalprice = totalprice;
    }

    private  Double totalprice;

}
