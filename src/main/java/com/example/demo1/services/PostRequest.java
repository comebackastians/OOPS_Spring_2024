package com.example.demo1.services;


public class PostRequest 
{
    private String postBody;
    private int userID;
    private int postID;

    public String getPostBody() {
        return postBody;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}