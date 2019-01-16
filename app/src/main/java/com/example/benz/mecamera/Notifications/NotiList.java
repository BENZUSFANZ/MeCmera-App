package com.example.benz.mecamera.Notifications;

public class NotiList {

    int status, id_booking, id_store, user_fk, customer_fk;
    String name;
    String im_profile;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId_booking() {
        return id_booking;
    }

    public void setId_booking(int id_booking) {
        this.id_booking = id_booking;
    }

    public int getId_store() {
        return id_store;
    }

    public void setId_store(int id_store) {
        this.id_store = id_store;
    }

    public int getUser_fk() {
        return user_fk;
    }

    public void setUser_fk(int user_fk) {
        this.user_fk = user_fk;
    }

    public int getCustomer_fk() {
        return customer_fk;
    }

    public void setCustomer_fk(int customer_fk) {
        this.customer_fk = customer_fk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIm_profile() {
        return im_profile;
    }

    public void setIm_profile(String im_profile) {
        this.im_profile = im_profile;
    }


}
