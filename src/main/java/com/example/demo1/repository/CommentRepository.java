package com.example.demo1.repository;

import com.example.demo1.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> 
{
    
}
