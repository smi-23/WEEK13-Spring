package com.example.springfirstproject.entity;

import com.example.springfirstproject.dto.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String username;

//    @Column(name = "user_id", nullable = false)
//    private Long userId;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<Comment> commentList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Post(PostRequestDto requestDto, String username, User user) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.username = username;
        this.user = user;

    }
    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
