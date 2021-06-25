package com.example.digitalshop.Model;

public class Analytics
{
    private String id;
    private  Integer views;
    private  Integer clicks;
    private  Integer orders;
    private  Integer impressions;

    public  Analytics(){}

    public  Analytics(String uid)
    {
        this.id=uid;
        setClicks(0);
        setEarning(0.0);
        setImpressions(0);
        setOrders(0);
        setViews(0);
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public Integer getViews () {
        return views;
    }

    public void setViews (Integer views) {
        this.views = views;
    }

    public Integer getClicks () {
        return clicks;
    }

    public void setClicks (Integer clicks) {
        this.clicks = clicks;
    }

    public Integer getOrders () {
        return orders;
    }

    public void setOrders (Integer orders) {
        this.orders = orders;
    }

    public Integer getImpressions () {
        return impressions;
    }

    public void setImpressions (Integer impressions) {
        this.impressions = impressions;
    }

    public Double getEarning () {
        return earning;
    }

    public void setEarning (Double earning) {
        this.earning = earning;
    }

    private  Double earning;



}
