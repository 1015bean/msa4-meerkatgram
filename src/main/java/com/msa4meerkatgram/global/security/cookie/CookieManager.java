package com.msa4meerkatgram.global.security.cookie;

import com.msa4meerkatgram.global.security.jwt.JwtConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class CookieManager {

    private final JwtConfig jwtConfig;

    // Request Header에서 특정 쿠키 획득(Optional 반환)
        // Optional 타입:
        // 반환값이 NULL이 될 경우 NullPointException이 일어날 수 있음.
        // Optional타임은 null처리를 해주지 않으면 오류가 나도록 설계됨
    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        // 쿠키 존재여부 확인
        if(request.getCookies() == null) {
            return Optional.empty();
        }

        // name이 일치하는 쿠키 획득
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst();
    }

    // 쿠키 생성 메소드
    public void setCookie(HttpServletResponse response, String name, String value, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value); // 해당 이름과 값으로 쿠키 인스턴스 생성
        cookie.setPath(path); // 쿠키를 사용할 path설정
        cookie.setMaxAge(maxAge); // 쿠키 유효시간 설정
        cookie.setHttpOnly(true); // HttpOnly 설정: XSS 공격 방지
        cookie.setSecure(jwtConfig.secure()); // 시큐어 설정: MITM 공격 방지(전송 중에 쿠키 탈취를 막기 위해, 암호화된 https로만 쿠키 전송)

        response.addCookie(cookie);
    }

}
