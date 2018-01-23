package com.appmilitia.firebuds.activity;

/**
 * Created by Salman on 13-01-2018.
 */

public class User {
    String user_fb_first_name,user_fb_last_name,user_fb_email,user_fb_id,user_fb_birthday,user_fb_gender,user_firebudsstatus,timestamp;
    Double user_current_couble_latti,user_current_couble_longi;

    public  User(){

    }

    public User(String user_fb_first_name, String user_fb_last_name, String user_fb_email, String user_fb_id, String user_fb_birthday, String user_fb_gender, String user_firebudsstatus, Double user_current_couble_latti, Double user_current_couble_longi,String timestamp) {
        this.user_fb_first_name = user_fb_first_name;
        this.user_fb_last_name = user_fb_last_name;
        this.user_fb_email = user_fb_email;
        this.user_fb_id = user_fb_id;
        this.user_fb_birthday = user_fb_birthday;
        this.user_fb_gender = user_fb_gender;
        this.user_firebudsstatus = user_firebudsstatus;
        this.user_current_couble_latti = user_current_couble_latti;
        this.user_current_couble_longi = user_current_couble_longi;
        this.timestamp=timestamp;
    }

    public String getUser_fb_first_name() {
        return user_fb_first_name;
    }

    public String getUser_fb_last_name() {
        return user_fb_last_name;
    }

    public String getUser_fb_email() {
        return user_fb_email;
    }

    public String getUser_fb_id() {
        return user_fb_id;
    }

    public String getUser_fb_birthday() {
        return user_fb_birthday;
    }

    public String getUser_fb_gender() {
        return user_fb_gender;
    }

    public String getUser_firebudsstatus() {
        return user_firebudsstatus;
    }

    public Double getUser_current_couble_latti() {
        return user_current_couble_latti;
    }

    public Double getUser_current_couble_longi() {
        return user_current_couble_longi;
    }

    public String getTimestamp(){
        return timestamp;
    }
}
