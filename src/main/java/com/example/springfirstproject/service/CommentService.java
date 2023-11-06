package com.example.springfirstproject.service;

import com.example.springfirstproject.dto.CommentRequestDto;
import com.example.springfirstproject.dto.CommentResponseDto;
import com.example.springfirstproject.entity.Comment;
import com.example.springfirstproject.entity.Post;
import com.example.springfirstproject.entity.User;
import com.example.springfirstproject.entity.UserRoleEnum;
import com.example.springfirstproject.jwt.JwtUtil;
import com.example.springfirstproject.repository.CommentRepository;
import com.example.springfirstproject.repository.PostRepository;
import com.example.springfirstproject.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    // 게시글의 댓글 생성
    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        Claims claims = getClaimsAndCheckToken(request);

        // JWT 토큰에서 추출한 사용자 이름을 사용하여 데이터베이스에서 사용자를 찾음
        User user = getUser(claims);
        // 인자로 받은 게시글 ID를 사용하여 게시물을 데이터베이스에서 찾음
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 게시글은 존재하지 않습니다.")
        );
        // Comment 엔티티를 생성하고 데이터베이스에 즉시 저장
        Comment comment = commentRepository.saveAndFlush(new Comment(requestDto, user.getUsername(), post, user));
        return new CommentResponseDto("댓글이 정상적으로 작성되었습니다.", HttpStatus.OK.value(), comment);
    }

    // 게시글의 댓글 수정
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        Claims claims = getClaimsAndCheckToken(request);
        User user = getUser(claims);
        Comment comment = getComment(id);

        UserRoleEnum userRoleEnum = user.getRole();
        if (userRoleEnum == UserRoleEnum.USER) {
            if (!comment.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
            }
        }
//        comment.update(requestDto, user.getUsername());
        comment.update(requestDto);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDto("댓글이 수정되었습니다.", HttpStatus.OK.value(), saveComment);
    }

    // 게시글의 댓글 삭제
    @Transactional
    public CommentResponseDto deleteComment(Long id, HttpServletRequest request) {
        Claims claims = getClaimsAndCheckToken(request);
        User user = getUser(claims);
        Comment comment = getComment(id);

        UserRoleEnum userRoleEnum = user.getRole();
        if (userRoleEnum == UserRoleEnum.USER) {
            if (!comment.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
            }
        }
        commentRepository.deleteById(id);
        return new CommentResponseDto("댓글이 삭제되었습니다.", HttpStatus.OK.value());
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
    // 헬퍼 함수: 댓글 검색
    private Comment getComment(Long id) {
        return  commentRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 댓글을 찾을 수 없습니다.")
        );
    }
}
