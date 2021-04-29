package com.botics.soundpay.Models;

import java.util.Date;

public class HistoryModel {
    public HistoryModel(){

    }

    String uid,amount,type;
    Date date;




    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public HistoryModel(String uid, String amount, String type, Date date) {
        this.uid = uid;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

}
