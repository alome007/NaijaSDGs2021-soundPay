package com.botics.soundpay.Models;

public class Contacts {
    public Contacts(){

    }

    String name,url;
    String number;
    boolean isContact;
    String username;
    String uid;
    String password;
    String accountNumber;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Contacts(String name, String number, String url, boolean isContact, String email, String uid, String username, String password, String  accountNumber) {
        this.name = name;
        this.number = number;
        this.url = url;
        this.isContact = isContact;
        this.email=email;
        this.uid=uid;
        this.username=username;
        this.password=password;
        this.accountNumber=accountNumber;
    }
}
