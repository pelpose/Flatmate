package com.example.pelpo.flatee;

import java.util.Date;

/**
 * Created by pelpo on 2017-05-30.
 */

public class ChatMessage {
    private String meessageUser;
    private String messageText;
    private long messageTime;

    public ChatMessage (String messageText, String meessageUser){
        this.meessageUser =meessageUser;
        this.messageText =messageText;

        messageTime = new Date().getTime();
    }

    public ChatMessage () {

    }

    public String getMeessageUser() {
        return meessageUser;
    }

    public void setMeessageUser(String meessageUser) {
        this.meessageUser = meessageUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}

