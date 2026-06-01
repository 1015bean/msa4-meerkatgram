package com.msa4meerkatgram.domain.auth.controllers;

import com.msa4meerkatgram.domain.auth.requests.LoginReq;
import com.msa4meerkatgram.domain.auth.responses.AuthRes;
import com.msa4meerkatgram.domain.auth.services.AuthService;
import com.msa4meerkatgram.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    // 로그인 요청
    @PostMapping("/login")
    public ResponseEntity<GlobalRes<AuthRes>> login(
            // @RequestBody: HTTP body에 Json형식 데이터 보내면, 받은 데이터를 데이터 객체(loginReq)에 담아줌
            // HttpServletResponse: 리퀘스트에 대해, 레스폰스할 때 필요한 정보(유저에게 반환할 쿠키)를 담을 객체
            @Valid @RequestBody LoginReq loginReq
            , HttpServletResponse response
    ) {
        return ResponseEntity.status(200).body(
                GlobalRes.<AuthRes>builder()
                        .code("00")
                        .message("로그인 완료")
                        .data(authService.login(response, loginReq))
                        .build()
        );
    }

    // 엑세스토큰 재발급
    // HttpServletResponse: 리퀘스트 정보 담길 변수
    // HttpServletResponse: 반환할 정보 담을 변수
    @PostMapping("/reissue-token")
    public ResponseEntity<GlobalRes<AuthRes>> reissue(
            HttpServletRequest request
            ,HttpServletResponse response
    ) {
        return ResponseEntity.status(200).body(
                GlobalRes.<AuthRes>builder()
                        .code("00")
                        .message("토큰 재발급 완료")
                        .data(authService.reissue(request, response))
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<GlobalRes<String>> logout(
            HttpServletResponse response
            , @AuthenticationPrincipal Claims claims
    ) {
        authService.logout(response, Long.parseLong((claims.getSubject())));

        return ResponseEntity.status(200).body(
                GlobalRes.<String>builder()
                        .code("00")
                        .message(("로그아웃 완료"))
                        .build()
        );
    }
}
