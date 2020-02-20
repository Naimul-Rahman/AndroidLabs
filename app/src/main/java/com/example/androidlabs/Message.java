package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

public class Message extends AppCompatActivity {

    private String message;
    private boolean send;
    private long id;

    public Message(String message, boolean send){this(message, send, 0);}

    public Message(String message, boolean send, long id){
        setMessage(message);
        setSend(send);
        setId(id);
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

    public void setId(long id){this.id = id;}

    public long getId(){ return this.id;}


}
