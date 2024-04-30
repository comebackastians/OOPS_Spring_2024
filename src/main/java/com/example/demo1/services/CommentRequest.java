package com.example.demo1.services;

public class CommentRequest 
{
    private String commentBody;
    private int postID;
    private int userID;
    private int commentID;

    public String getCommentBody() {
        return commentBody;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}