package com.example.springfirstproject.controller;

import com.example.springfirstproject.dto.CommentRequestDto;
import com.example.springfirstproject.dto.CommentResponseDto;
import com.example.springfirstproject.repository.CommentRepository;
import com.example.springfirstproject.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    // 코멘트 생성
    @PostMapping("/comment/{id}")
    public CommentResponseDto creatComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        try {
            return commentService.createComment(id, requestDto, request);
        } catch (Exception e) {
            return new CommentResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }

    // 코멘트 업데이트
    @PutMapping("/comment/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        try {
            return commentService.updateComment(id, requestDto, request);
        } catch (Exception e) {
            return new CommentResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }

    // 코멘트 삭제
    @DeleteMapping("/comment/{id}")
    public CommentResponseDto deleteComment(@PathVariable Long id, HttpServletRequest request) {
        try {
            return commentService.deleteComment(id, request);
        } catch (Exception e) {
            return new CommentResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }
}
