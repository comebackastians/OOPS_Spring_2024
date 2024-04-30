package com.example.demo1.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int commentID;

    @Column(name = "comment_body", nullable = false)
    private String commentBody;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_creator_id", nullable = false)
    private User commentCreator;

    // Constructors, getters, and setters...

    public Comment(String commentBody, User commentCreator) {
        this.commentBody = commentBody;
        this.commentCreator = commentCreator;
    }

    public Comment(String commentBody, User commentCreator, Post post) {
        this.commentBody = commentBody;
        this.commentCreator = commentCreator;
        this.post = post;
    }

    public int getCommentID() {
        return commentID;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public User getCommentCreator() {
        return commentCreator;
    }

    public Post getPost() {
        return post;
    }
}
