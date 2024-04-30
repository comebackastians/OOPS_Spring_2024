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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.example.demo1.services.PostRequest;

@RestController
@RequestMapping("")
public class PostController
{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    // POST endpoint...working
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostRequest request) {
        //try {
            Optional<User> optionalUser = userRepository.findById(request.getUserID());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Post newPost = new Post(request.getPostBody(), user);
                postRepository.save(newPost); // Save the new post to the database
                return ResponseEntity.ok("Post created successfully");
            } 
            else 
            {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Error", "User does not exist" );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
            }
        //} 
        // catch (Exception e) {
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        // }
    }

    @GetMapping("/post")
    public ResponseEntity<?> getPost(@RequestParam("postID") int postID) {
        // Retrieve post from the database
        Post post = postRepository.findById(postID).orElse(null);

        if (post != null) {
            Map<String, Object> response = new LinkedHashMap<>(); // Using LinkedHashMap to maintain the order
            response.put("postID", post.getPostID());
            response.put("postBody", post.getPostBody());
            response.put("date", post.getDate());

            // Include comments for the post
            List<Comment> postComments = post.getComments();
            if (!postComments.isEmpty()) {
                List<Map<String, Object>> postCommentsDetails = new ArrayList<>();
                for (Comment comment : postComments) {
                    Map<String, Object> commentDetails = new LinkedHashMap<>(); // Using LinkedHashMap to maintain the order
                    commentDetails.put("commentID", comment.getCommentID());
                    commentDetails.put("commentBody", comment.getCommentBody());

                    // Include comment creator details
                    User commentCreator = comment.getCommentCreator();
                    if (commentCreator != null) {
                        Map<String, Object> creatorDetails = new LinkedHashMap<>(); // Using LinkedHashMap to maintain the order
                        creatorDetails.put("userID", commentCreator.getUserID());
                        creatorDetails.put("name", commentCreator.getName());
                        commentDetails.put("commentCreator", creatorDetails);
                    }

                    postCommentsDetails.add(commentDetails);
                }
                response.put("comments", postCommentsDetails);
            } else {
                // No comments for the post
                response.put("comments", new ArrayList<>());
            }

            return ResponseEntity.ok(response);
        } else {
            // Post not found
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }



    // PATCH POST endpoint...working..ok
    @PatchMapping("/post")
    public ResponseEntity<?> editPost(@RequestBody PostRequest request) {
        int postID = request.getPostID();
        Post post = postRepository.findById(postID).orElse(null);
        if (post != null) 
        {
            post.setPostBody(request.getPostBody());
            postRepository.save(post); // Save the updated post
            return ResponseEntity.ok("Post edited successfully");
        } 
        else 
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Post does not exist" );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
        }
    }

    // DELETE POST endpoint ... working 
    // DELETE POST endpoint ... working 
    @DeleteMapping("/post")
    public ResponseEntity<?> deletePost(@RequestParam("postID") int postID) {
        Post post = postRepository.findById(postID).orElse(null);
        if (post != null) {
            postRepository.delete(post); // Delete the post from the database
            return ResponseEntity.ok("Post deleted");
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Post does not exist" );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}

