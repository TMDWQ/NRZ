package com.men_cloths.model;

/**
 * Created by Administrator on 2016/12/4.
 */

public class Commodity {
    private String name;
    private String rule;
    private String price;
    private String iamge;
    private String id;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIamge() {
        return iamge;
    }
    public  Commodity(){}


    public Commodity(String name, String rule, String price, String iamge, String id) {
        this.name = name;
        this.rule = rule;
        this.price = price;
        this.iamge = iamge;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRule() {
        return rule;
    }

    public String getPrice() {
        return price;
    }
}
