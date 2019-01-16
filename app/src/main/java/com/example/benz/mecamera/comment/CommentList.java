package com.example.benz.mecamera.comment;

public class CommentList {

    int id_user;
    String Name;
    String Comment;
    String imProfile;



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getImProfile() {
        return imProfile;
    }

    public void setImProfile(String imProfile) {
        this.imProfile = imProfile;
    }



}
