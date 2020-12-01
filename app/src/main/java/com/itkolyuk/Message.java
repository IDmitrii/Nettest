package com.itkolyuk;

public class Message {
    public String text;
    public String user_id;


    public  Message(){}

    public Message(String t,String u){

        this.text = t;
        this.user_id=u;

    }


    public String getText() {
    return text;
}

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }




    public void setText(String text) {
        this.text = text;
    }
}
