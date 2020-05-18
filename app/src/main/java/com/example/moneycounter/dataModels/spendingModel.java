package com.example.moneycounter.dataModels;

import java.util.Date;
import java.util.List;

public class spendingModel {
    private String[] receiptNumbersList;
    private String note;
    private double sum;
    private Date spendingsDate;
    private String spendingName;

    public spendingModel(String[] receiptModelList, double sum, Date spendingsDate, String note, String name) {
        this.receiptNumbersList = receiptModelList;
        this.sum = sum;
        this.note = note;
        this.spendingsDate = spendingsDate;
        this.spendingName = name;
    }
    public spendingModel(){}

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public Date getSpendingsDate() {
        return spendingsDate;
    }

    public void setSpendingsDate(Date spendingsDate) {
        this.spendingsDate = spendingsDate;
    }
    public String[] getReceiptNumbersList() {
        return receiptNumbersList;
    }

    public void setReceiptNumbersList(String[] receiptNumbersList) {
        this.receiptNumbersList = receiptNumbersList;
    }

    public String getSpendingName() {
        return spendingName;
    }

    public void setSpendingName(String spendingName) {
        this.spendingName = spendingName;
    }
}
