package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

public class Message extends AppCompatActivity {

    private String message;
    private boolean send;

    public Message(String message, boolean send){
        setMessage(message);
        setSend(send);
    }


    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public void setSend(boolean send){
        this.send = send;
    }

    public boolean getSend(){
        return this.send;
    }


}
