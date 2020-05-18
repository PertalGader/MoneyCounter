package com.example.moneycounter.dataModels;

public class tovarModel {
    private int count;
    private double price;
    private String tovarName;

    public tovarModel(int count, double price, String tovarName) {
        this.count = count;
        this.price = price;
        this.tovarName = tovarName;
    }
    public tovarModel(){

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTovarName() {
        return tovarName;
    }

    public void setTovarName(String tovarName) {
        this.tovarName = tovarName;
    }
}
