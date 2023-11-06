package com.example.springfirstproject.service;

import com.example.springfirstproject.dto.LoginRequestDto;
import com.example.springfirstproject.dto.MsgResponseDto;
import com.example.springfirstproject.dto.SignupRequestDto;
import com.example.springfirstproject.entity.User;
import com.example.springfirstproject.entity.UserRoleEnum;
import com.example.springfirstproject.jwt.JwtUtil;
import com.example.springfirstproject.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

//import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
//    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원가입
    @Transactional
    public MsgResponseDto signup(@Valid SignupRequestDto signupRequestDto) { //@valid 일단 빠짐
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
//        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        if (username.contains(" ")) {
            throw new IllegalArgumentException("아이디에 공백을 포함할 수 없습니다.");
        }

        if (password.contains(" ")) {
            throw new IllegalArgumentException("비밀번호에 공백을 포함할 수 없습니다.");
        }

        // 아이디 정규식 확인
        if (!username.matches("^[a-z0-9]{4,10}$")) {
            throw new IllegalArgumentException("아이디는 4자 이상, 10자 이하 알파벳 소문자, 숫자로 이루어져야 합니다.");
        }

        // 비밀번호 정규식 확인
        if (!password.matches("[a-zA-Z0-9`~!@#$%^&*()_=+|{};:,.<>/?]*$")) {
            throw new IllegalArgumentException("비밀번호는 8자 이상, 15자 이하 알파벳 대/소문자, 숫자, 특수문자로 이루어져야 합니다.");
        }

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다. 다른 아이디를 선택해주세요.");
        }

        // 사용자 Role 확인
        // 입력값으로 어드민 트루펄스를 받는데 토큰을 입력하고 트루펄스를 입력하지 않으면 그냥 유저로 회원가입이 됨 수정해야할듯?
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 권한을 부여하려면 올바른 ADMIN_TOKEN을 입력해주세요.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 조회한 내용이 DB에 없을 경우, 사용자 등록(admin = false 일 경우 아래 코드 수정)
        User user = new User(username, password, role);
        userRepository.save(user);
        return new MsgResponseDto("회원가입이 완료되었습니다. 환영합니다!", HttpStatus.OK.value());
    }

    // 로그인
    @Transactional(readOnly = true)
    public MsgResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록되지 않은 사용자입니다.")
        );

        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        System.out.println(response);
        return new MsgResponseDto("로그인되었습니다. 환영합니다!", HttpStatus.OK.value());
    }
}
