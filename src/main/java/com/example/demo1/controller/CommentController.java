package com.example.demo1.controller;
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
import com.example.demo1.services.CommentRequest;

@RestController
@RequestMapping("")
public class CommentController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest request) {
        // Find the user
        User user = userRepository.findById(request.getUserID()).orElse(null);
        if (user != null) 
        {
            // Find the post
            Post post = postRepository.findById(request.getPostID()).orElse(null);
            if (post != null) 
            {
                // Create a new comment
                Comment newComment = new Comment(request.getCommentBody(), user, post);
                // Add the comment to the post and user entities
                post.getComments().add(newComment);
                //user.getComments().add(newComment);
                // Save the updated post and user entities to the database
                postRepository.save(post);
                //userRepository.save(user);
                return ResponseEntity.ok("Comment created successfully");
            } 
            else 
            {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Error", "Post does not exist" );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
            }
        } 
        else 
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist" );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

    @GetMapping("/comment")
    public ResponseEntity<?> getComment(@RequestParam("commentID") int commentID) {
        Optional<Comment> optionalComment = commentRepository.findById(commentID);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            Map<String, Object> response = new LinkedHashMap<>(); // Using LinkedHashMap to maintain order
            response.put("commentID", comment.getCommentID());
            response.put("commentBody", comment.getCommentBody());

            // Include details of the comment creator
            User commentCreator = comment.getCommentCreator();
            if (commentCreator != null) {
                Map<String, Object> creatorDetails = new LinkedHashMap<>(); // Using LinkedHashMap to maintain order
                creatorDetails.put("userID", commentCreator.getUserID());
                creatorDetails.put("name", commentCreator.getName());
                response.put("commentCreator", creatorDetails);
            }
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new LinkedHashMap<>(); // Using LinkedHashMap to maintain order
            errorResponse.put("Error", "Comment does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> editComment(@RequestBody CommentRequest request) {
        Optional<Comment> optionalComment = commentRepository.findById(request.getCommentID());
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setCommentBody(request.getCommentBody());
            commentRepository.save(comment); // Save the updated comment to the database
            return ResponseEntity.ok("Comment edited successfully");
        } 
        else 
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Comment does not exist" );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment does not exist");
        }
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(@RequestParam("commentID") int commentID) 
    {
        Optional<Comment> optionalComment = commentRepository.findById(commentID);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            Post post = comment.getPost(); // Get the associated post
            if (post != null) {
                // Remove the comment from the associated post's comment list
                List<Comment> postComments = post.getComments();
                postComments.removeIf(c -> c.getCommentID() == commentID);
                postRepository.save(post); // Update the post in the database
            }
            // Delete the comment from the database
            commentRepository.delete(comment);
            return ResponseEntity.ok("Comment deleted");
        } 
        else 
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Comment does not exist" );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}