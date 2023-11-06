package com.example.springfirstproject.dto;

import com.example.springfirstproject.entity.Comment;
import com.example.springfirstproject.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Getter
@NoArgsConstructor
@Configuration
public class CommentResponseDto extends MsgResponseDto {
    CommentDetailsDto commentList; // CommentToDto 연결

    // 생성자(mgs+Statuscode)
    public CommentResponseDto(String msg, int statusCode) {
        super(msg, statusCode);
    }

    // 생성자(mgs+Statuscode+Entity)
    public CommentResponseDto (String msg, int statusCode, Comment comment) {
        super(msg, statusCode);
        this.commentList = new CommentDetailsDto(comment);
    }
}
