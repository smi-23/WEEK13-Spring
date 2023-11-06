package com.example.springfirstproject.dto;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class PostListResponseDto extends MsgResponseDto {
    List<PostDetailsDto> postList = new ArrayList<>();

    public PostListResponseDto(String msg, int statusCode) {
        super(msg, statusCode);
    }

    public void add(PostDetailsDto postToDto) {
        postList.add(postToDto);
    }
}
