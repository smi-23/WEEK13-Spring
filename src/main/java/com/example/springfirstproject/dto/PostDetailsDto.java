package com.example.springfirstproject.dto;

import com.example.springfirstproject.entity.Comment;
import com.example.springfirstproject.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
public class PostDetailsDto {            // entity -> DTO
    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private ArrayList<Comment> commentList;

    public PostDetailsDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.content = post.getContents();
        this.createdAt = post.getCreatedAt();
    }

    public PostDetailsDto(Post post, ArrayList<Comment> commentList) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.content = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.commentList = (ArrayList<Comment>) commentList.clone();
    }
}
