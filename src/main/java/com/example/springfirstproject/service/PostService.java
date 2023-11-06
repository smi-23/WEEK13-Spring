package com.example.springfirstproject.service;

import com.example.springfirstproject.dto.*;
import com.example.springfirstproject.entity.Comment;
import com.example.springfirstproject.entity.User;
import com.example.springfirstproject.entity.Post;

import com.example.springfirstproject.entity.UserRoleEnum;
import com.example.springfirstproject.jwt.JwtUtil;
import com.example.springfirstproject.repository.CommentRepository;
import com.example.springfirstproject.repository.PostRepository;
import com.example.springfirstproject.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    // 포스트 생성
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        Claims claims = getClaimsAndCheckToken(request);

        User user = getUser(claims);

        Post post = postRepository.saveAndFlush(new Post(requestDto, user.getUsername(), user));
        return new PostResponseDto("게시글이 정상적으로 작성되었습니다.", HttpStatus.OK.value(), post);
    }

    // 포스트 조회
    @Transactional(readOnly = true)
    public PostListResponseDto getPostList() {

        PostListResponseDto postListResponseDto = new PostListResponseDto("전체 게시글을 조회합니다.", HttpStatus.OK.value());

        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        for (Post post : posts) {
            List<Comment> commentList = commentRepository.findAllByPost(post);
            Collections.reverse(commentList);
            postListResponseDto.add(new PostDetailsDto(post, (ArrayList<Comment>) commentList));
        }
        return postListResponseDto;
    }

    // 선택 포스트 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPostListOne(Long id) {
        Post post = getPost(id);
        List<Comment> commentList = commentRepository.findAllByPost(post);
        Collections.reverse(commentList);
        return new PostResponseDto("선택한 게시글을 조회합니다.", HttpStatus.OK.value(), post, (ArrayList<Comment>) commentList);
    }

    // 포스트 업데이트
    @Transactional
    public PostResponseDto update(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        Claims claims = getClaimsAndCheckToken(request);
        User user = getUser(claims);
        Post post = getPost(id);

        UserRoleEnum userRoleEnum = user.getRole();
        if (userRoleEnum == UserRoleEnum.USER) {
            if (!post.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
            }
        }
        post.update(requestDto);
        Post savePost = postRepository.save(post);
        return new PostResponseDto("게시글이 수정되었습니다.", HttpStatus.OK.value(), savePost);
    }

    // 포스트 삭제
    @Transactional
    public MsgResponseDto deletePost(Long id, HttpServletRequest request) {
        Claims claims = getClaimsAndCheckToken(request);
        User user = getUser(claims);
        Post post = getPost(id);

        UserRoleEnum userRoleEnum = user.getRole();
        if (userRoleEnum == UserRoleEnum.USER) {
            if (!post.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
            }
        }
        postRepository.deleteById(id);

        return new PostResponseDto("게시글이 삭제되었습니다.", HttpStatus.OK.value());
    }

    // 헬퍼 함수: 토큰 검증 및 사용자 정보 가져오기
    private Claims getClaimsAndCheckToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token == null)
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        if (jwtUtil.validateToken(token)) {
            claims = jwtUtil.getUserInfoFromToken(token);
        } else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        return claims;
    }

    // 헬퍼 함수: 사용자 검색
    private User getUser(Claims claims) {
        return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.")
        );
    }
    // 헬퍼 함수: 포스트 검색
    private Post getPost(Long id) {
        return  postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 게시글을 찾을 수 없습니다.")
        );
    }
}
