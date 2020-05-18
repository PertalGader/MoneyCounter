package com.example.moneycounter.dataModels;

import java.util.List;

public class receiptModel {
    private List<tovarModel> tovarModelList;
    private String owner;
    private double receiptSum;

    public receiptModel(List<tovarModel> tovarModelList, String receiptNumber, double receiptSum) {
        this.tovarModelList = tovarModelList;
        this.owner = receiptNumber;
        this.receiptSum = receiptSum;
    }
    public receiptModel(){}

    public List<tovarModel> getTovarModelList() {
        return tovarModelList;
    }

    public void setTovarModelList(List<tovarModel> tovarModelList) {
        this.tovarModelList = tovarModelList;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String receiptNumber) {
        this.owner = receiptNumber;
    }

    public double getReceiptSum() {
        return receiptSum;
    }

    public void setReceiptSum(double receiptSum) {
        this.receiptSum = receiptSum;
    }

    public void setReceiptSum(List<tovarModel> tovarModelList){
        double sum = 0;
        for(int i = 0;i < tovarModelList.size();i++){
            sum += tovarModelList.get(i).getPrice();
        }
        receiptSum = sum;
    }
}
