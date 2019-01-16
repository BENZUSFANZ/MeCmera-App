package com.example.benz.mecamera;

public class ipConfig {

    public String Url= "http://10.0.2.2/MeCamera/User-android/";

    public ipConfig() {

    }

    public String getUrl() {
        return Url;
    }

    public String getUrlrcvStore() {
        return Url +"RecyleViewStore/";
    }

    public String getUrlrcvMyStore() {
        return Url +"RecyleViewMyStore/";
    }

    public String getUrlDtStore() {
        return Url +"DetailStore/";
    }

    public String getUrlBooking(){
        return Url +"Booking/";
    }

    public String getUrlBlogPost(){
        return Url +"BlogPost/";
    }

    public String getUrlNotistore(){
        return Url +"NotificationStore/";
    }

    public String getUrlMapStore(){
        return Url +"MapStore/";
    }

    public String getUrlSearch(){
        return Url +"Search/";
    }

    public String getUrlLikeComment(){
        return Url +"LikeComment/";
    }

}
