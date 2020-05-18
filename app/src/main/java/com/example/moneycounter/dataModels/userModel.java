package com.example.moneycounter.dataModels;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class userModel {
    private String email, town;
    private Date birthdayDate;
    private Map<String, Object> name;
    private List<String> categoriesArray;

    public List<String> getCategoriesArray() {
        return categoriesArray;
    }

    public void setCategoriesArray(List<String> categoriesArray) {
        this.categoriesArray = categoriesArray;
    }

    public userModel(){
    }


    public userModel(String email, Map<String, Object> name, String town, Date birthdayDate) {
        this.email = email;
        this.name = name;
        this.town = town;
        this.birthdayDate = birthdayDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getName() {
        return name;
    }

    public void setName(Map<String, Object> name) {
        this.name = name;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }
}
