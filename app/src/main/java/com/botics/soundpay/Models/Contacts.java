package com.botics.soundpay.Models;

public class Contacts {
    public Contacts(){

    }

    String name,number,url;
    boolean isContact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isContact() {
        return isContact;
    }

    public void setContact(boolean contact) {
        isContact = contact;
    }

    public Contacts(String name, String number, String url, boolean isContact) {
        this.name = name;
        this.number = number;
        this.url = url;
        this.isContact = isContact;
    }
}
