package com.example.springfirstproject.repository;

import com.example.springfirstproject.entity.Comment;
import com.example.springfirstproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
