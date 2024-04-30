package com.example.demo1.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo1.model.Comment;
import com.example.demo1.model.Post;
import com.example.demo1.model.User;
import com.example.demo1.repository.CommentRepository;
import com.example.demo1.repository.PostRepository;
import com.example.demo1.repository.UserRepository;

@RestController
@RequestMapping("")
public class UserController
{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    // LOGIN endpoint 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) 
        {
            return ResponseEntity.ok("Login Successful");
        } 
        else if (foundUser != null) 
        {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Username/Password Incorrect" );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
         
        else 
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    // SIGNUP endpoint 
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        // Check if user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Forbidden,Account already exists");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        
        // Save the user to the database
        userRepository.save(user);
        return ResponseEntity.ok("Account Creation Successful");
    }

    // getuser 
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@RequestParam("userID") int userID) 
    {
        Optional<User> optionalUser = userRepository.findById(userID);
        if (optionalUser.isPresent()) 
        {
            User user = optionalUser.get();
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("userID", user.getUserID());
            userDetails.put("name", user.getName());
            userDetails.put("email", user.getEmail());

            // Include posts details
            // List<Map<String, Object>> userPosts = new ArrayList<>();
            // for (Post post : user.getPosts()) 
            // {
            //     Map<String, Object> postDetails = new HashMap<>();
            //     postDetails.put("postID", post.getPostID());
            //     postDetails.put("postBody", post.getPostBody());
            //     postDetails.put("date", post.getDate());

            //     // Include comments details for each post
            //     List<Map<String, Object>> postComments = new ArrayList<>();
            //     for (Comment comment : post.getComments()) {
            //         Map<String, Object> commentDetails = new HashMap<>();
            //         commentDetails.put("commentID", comment.getCommentID());
            //         commentDetails.put("commentBody", comment.getCommentBody());

            //         // Include comment creator details
            //         User commentCreator = comment.getCommentCreator();
            //         if (commentCreator != null) {
            //             Map<String, Object> creatorDetails = new HashMap<>();
            //             creatorDetails.put("userID", commentCreator.getUserID());
            //             creatorDetails.put("name", commentCreator.getName());
            //             commentDetails.put("commentCreator", creatorDetails);
            //         }
            //         postComments.add(commentDetails);
            //     }
            //     postDetails.put("comments", postComments);
            //     userPosts.add(postDetails);
            // }
            //userDetails.put("posts", userPosts);
            return ResponseEntity.ok(userDetails);
        } 
        else 
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

    // USER FEED endpoint
    @GetMapping("/")
    public ResponseEntity<?> getUserFeed() {
        // Retrieve posts from the database in reverse chronological order
        List<Post> posts = postRepository.findAllByOrderByPostIDDesc();

        List<Map<String, Object>> response = new ArrayList<>();

        // Iterate through each post and construct the response
        for (Post post : posts) {
            Map<String, Object> postDetails = new LinkedHashMap<>(); // Using LinkedHashMap to maintain order
            postDetails.put("postID", post.getPostID());
            postDetails.put("postBody", post.getPostBody());
            postDetails.put("date", post.getDate());

            // Include comments for each post
            List<Map<String, Object>> postComments = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentDetails = new LinkedHashMap<>(); // Using LinkedHashMap to maintain order
                commentDetails.put("commentID", comment.getCommentID());
                commentDetails.put("commentBody", comment.getCommentBody());

                // Include comment creator details
                User commentCreator = comment.getCommentCreator();
                if (commentCreator != null) {
                    Map<String, Object> creatorDetails = new LinkedHashMap<>(); // Using LinkedHashMap to maintain order
                    creatorDetails.put("userID", commentCreator.getUserID());
                    creatorDetails.put("name", commentCreator.getName());
                    commentDetails.put("commentCreator", creatorDetails);
                }

                postComments.add(commentDetails);
            }

            postDetails.put("comments", postComments);

            response.add(postDetails);
        }

        return ResponseEntity.ok(response);
    }


    // get all users 
    @GetMapping("/users")
    public List<Map<String, Object>> getAllUsers() {
        List<Map<String, Object>> allUsers = new ArrayList<>();
        // Retrieve all users from the database
        List<User> users = userRepository.findAll();
        // Iterate over each user and construct user details map
        for (User user : users) {
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("userID", user.getUserID());
            userDetails.put("name", user.getName());
            userDetails.put("email", user.getEmail());
            // You can include additional details as needed
            allUsers.add(userDetails);
        }
        return allUsers;
    }
}

