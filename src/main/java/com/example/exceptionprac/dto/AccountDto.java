package com.example.exceptionprac.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


public class AccountDto {

    @Getter
    @NoArgsConstructor
    public static class SignUpReq {
        // @Valid 기능을 통한 유효성 검사
        @NotEmpty (message = "username에 Null값 또는 공백 문자열을 허용하지 않습니다.")
        private String username;
        @NotBlank (message = "password에 Null값 또는 문자 1개 이상 포함되어야 합니다.")
        private String password;
        @Email (message = "@를 포함하여 올바른 이메일 형식을 적어주세요.")
        private String email;
    }

    @Getter
    @NoArgsConstructor
    public static class Res {
        private String username;
        private String email;

        public Res(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }

}
