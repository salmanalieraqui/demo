package com.appmilitia.firebuds.activity;

/**
 * Created by Salman on 16-01-2018.
 */

public class Msg {

    String random_push_value, user_fb_first_name,user_fb_last_name,textmessage,timestamp;
    public Msg(){

    }

    public Msg(String random_push_value,String user_fb_first_name, String user_fb_last_name, String textmessage,String timestamp) {
        this.random_push_value=random_push_value;
        this.user_fb_first_name = user_fb_first_name;
        this.user_fb_last_name = user_fb_last_name;
        this.textmessage = textmessage;
        this.timestamp=timestamp;
    }
    public String getRandom_push_value() {
        return random_push_value;
    }

    public String getUser_fb_first_name() {
        return user_fb_first_name;
    }

    public String getUser_fb_last_name() {
        return user_fb_last_name;
    }

    public String getTextmessage() {
        return textmessage;
    }
    public String getTimestamp(){
        return timestamp;
    }
}
