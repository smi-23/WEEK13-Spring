package com.example.springfirstproject.controller;

import com.example.springfirstproject.dto.LoginRequestDto;
import com.example.springfirstproject.dto.MsgResponseDto;
import com.example.springfirstproject.dto.SignupRequestDto;
import com.example.springfirstproject.entity.User;
import com.example.springfirstproject.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @PostMapping("/signup")
    public MsgResponseDto signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        try {
            return userService.signup(signupRequestDto);
        } catch (Exception e) {
            return new MsgResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());                         // 예외 발생시 에러 내용, Httpstatus(400)을 리턴값으로 전달한다.
        }
    }

    @PostMapping("/login")
    public MsgResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            return userService.login(loginRequestDto, response);
        } catch (Exception e) {
            return new MsgResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());                         // 예외 발생시 에러 내용, Httpstatus(400)을 리턴값으로 전달한다.
        }
    }
}
