package com.example.meritmatch;


import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("sender_id")
    private int senderId;
    @SerializedName("receiver_id")
    private int receiverId;
    @SerializedName("content")
    private String content;


    public int getSenderId() {
            return senderId;
    }

    public int getReceiverId() {
            return receiverId;
    }

    public String getContent() {
            return content;
    }


    public void setSenderId(int senderId) {
            this.senderId = senderId;
    }

    public void setReceiverId(int receiverId) {
            this.receiverId = receiverId;
    }

    public void setContent(String content) {
            this.content = content;
    }



}
