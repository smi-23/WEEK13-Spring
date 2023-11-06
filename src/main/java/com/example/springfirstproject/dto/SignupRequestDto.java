package com.example.springfirstproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
//    @NotBlank(message = "아이디를 입력해주세요")
//    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디를 4~10자로 입력해주세요.(특수문자x)")
    private String username;

//    @NotBlank(message = "비밀번호를 입력해주세요")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,15}$", message = "비밀번호를 8~15자로 입력해주세요.(특수문자o)")
    private String password;

    // 기본적으로 false로 설정되어 있어 일반 사용자로 초기화
    private boolean admin = false;
    // 초기값으로 빈 문자열을 가지고 있어서 회원 가입 요청 시에는 관리자 권한이 비활성화
    private String adminToken = "";
}
