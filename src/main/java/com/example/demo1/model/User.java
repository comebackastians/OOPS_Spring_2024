package com.example.demo1.model;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userID;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "commentCreator", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    //private int nextUserID = 1;

    // Constructors, getters, and setters...
    public User(String email, String name, String password) {
            //this.userID = nextUserID++;
            this.email = email;
            this.name = name;
            this.password = password;
            this.posts = new ArrayList<>();
            this.comments = new ArrayList<>();
        }

        public int getUserID() {
            return userID;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        public List<Post> getPosts() {
            return posts;
        }
        public List<Comment> getComments() {
            return comments;
        }
}
