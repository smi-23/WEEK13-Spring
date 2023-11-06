package com.example.springfirstproject.controller;

import com.example.springfirstproject.dto.*;
import com.example.springfirstproject.entity.Post;
import com.example.springfirstproject.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 홈 페이지 뷰를 렌더링
    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    // 새 포스트 생성 요청 처리
    @PostMapping("/api/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        try {
            return postService.createPost(requestDto, request);
        } catch (Exception e) {
            return new PostResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }

    // 모든 포스트 리스트 조회 요청 처리
    @GetMapping("/api/posts")
    public PostListResponseDto getPostList() {
        return postService.getPostList();
    }

    // 선택 포스트 리스트 조회 요청 처리
    @GetMapping("/api/posts/{id}")
    public PostResponseDto getPostListOne(@PathVariable Long id) {
        return postService.getPostListOne(id);
    }

    // 포스트 업데이트 요청 처리
    @PutMapping("/api/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        try {
            return postService.update(id, requestDto, request);
        } catch (Exception e) {
            return new  PostResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }

    // 포스트 삭제 요청 처리
    @DeleteMapping("/api/posts/{id}")
    public MsgResponseDto deletePost(@PathVariable Long id, HttpServletRequest request) {
        try {
            return postService.deletePost(id,request);
        } catch (Exception e) {
            return new MsgResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }
}
