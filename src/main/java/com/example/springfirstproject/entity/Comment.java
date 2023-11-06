package com.example.springfirstproject.entity;

import com.example.springfirstproject.dto.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Comment(CommentRequestDto requestDto, String username, Post post, User user) {
        this.username = username;
        this.contents = requestDto.getContents();
        this.post = post;
        this.user = user;
    }

//    public void update(CommentRequestDto requestDto, String username) {
//        this.contents = requestDto.getContents();
////        this.username = requestDto.getUsername();
//        this.username = username;
//    }
    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
//        this.username = requestDto.getUsername();
//        this.username = username;
    }
}
