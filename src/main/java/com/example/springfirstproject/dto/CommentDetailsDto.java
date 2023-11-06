package com.example.springfirstproject.dto;

import com.example.springfirstproject.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDetailsDto {
    private Long id;
    private String username;
    private String contents;
    private LocalDateTime createdAt;

    public CommentDetailsDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUsername();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
    }
}
