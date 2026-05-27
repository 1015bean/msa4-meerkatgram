package com.msa4meerkatgram.domain.auth.services;

import com.msa4meerkatgram.domain.auth.requests.LoginReq;
import com.msa4meerkatgram.domain.user.entities.User;
import com.msa4meerkatgram.domain.user.mapper.UserMapper;
import com.msa4meerkatgram.global.errors.custom.NotRegisteredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;

    public void login(LoginReq loginReq) {
        // 유저정보 획득
        User user = userMapper.findByEmail(loginReq.email());

        // 유저 가입여부 확인
            // (user가 없을 시) 에러 발생시킴
        if(user == null) {
            throw new NotRegisteredException("아이디와 비밀번호를 확인해 주세요.");
        }

        // 비밀번호 체크

        // 토큰 생성

        // 리프레시 토큰을 서버(DB)에 저장

        // 리프레시 토큰을 쿠키에 저장

        // 리턴
    }
}
