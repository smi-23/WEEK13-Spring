package com.example.springfirstproject.dto;

import com.example.springfirstproject.entity.Comment;
import com.example.springfirstproject.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
@Configuration
public class PostResponseDto extends MsgResponseDto {
   PostDetailsDto postList;

    // 생성자(mgs+Statuscode)
    public PostResponseDto(String msg, int statusCode) {
        super(msg, statusCode);
    }

    // 생성자(mgs+Statuscode+Entity)
    public PostResponseDto(String msg, int statusCode, Post post) {
        super(msg, statusCode);
        this.postList = new PostDetailsDto(post);
    }

    // 생성자(mgs+Statuscode+comment Entity)
    public PostResponseDto(String msg, int statusCode, Post post, ArrayList<Comment> commentList) {
        super(msg, statusCode);
        this.postList = new PostDetailsDto(post, commentList);
    }
}
