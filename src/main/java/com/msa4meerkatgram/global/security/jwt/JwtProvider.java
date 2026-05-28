package com.msa4meerkatgram.global.security.jwt;

import com.msa4meerkatgram.domain.user.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// @Component: 커스텀 bean 객체를 만들게 해주는 어노테이션
    // bean: 프레임워크에서 자동으로 인스턴스 생성해서 관리해주는 객체
@Component
public class JwtProvider {

    // JwtConfig: JWT 토큰의 세부 설정값을 담는 객체(JwtConfig의 설정값을 불러와서 이용(application.yaml 파일의 설정 값을 불러옴))
    // secretKey: 토큰의 Signature부분에 사용할 비밀키 객체
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    // 생성자 커스텀
        // Keys.hmacShaKeyFor() : 토큰의 signature 부분의 비밀키를 만드는(암호화하는) 메소드
        // Decoders.BASE64.decode(): 암호화된 부분을 디코딩하는 작업
    public JwtProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.secret()));
    }

    // JWT형식의 토큰을 생성해주는 메소드
        // ttl:  = Time To Live, 토큰의 유효기간을 밀리초 단위 표시
    private String generateToken(User user, long ttl) {
        Date now = new Date();

        // 리턴해줄 토큰의 형식 커스텀(JWT형식을 바탕으로)
        return Jwts.builder()
                .header() // 토큰의 Header(헤더부분) 셋팅
                .type(jwtConfig.type()) // 토큰의 타입 설정
                .and() // 헤더 설정 끝. 다시 토큰 빌더로 돌아가는 연결 메소드(이제부터 Payload부분 셋팅)
                .subject(String.valueOf(user.getId()))  // subject: 해당 토큰의 대상이 되는 주인(유저) 지정(여기서는 파라미터로 받은 유저ID를 주인으로 지정. long타입이므로 string타입으로 변환)
                .issuer(jwtConfig.issuer()) // 토큰 발급자(내 서비스 혹은 도메인)
                .issuedAt(now) // 토큰 발급시간
                .expiration(new Date(now.getTime() + ttl)) // 토큰 만료시간
                .claim("role", user.getRole()) // private claim: 커스텀 속성 설정
                .signWith(secretKey) // 토큰의 signature 부분
                .compact();
    }

    // Access Token 생성하는 메소드(위 메소드가 프라이빗이므로 외부서 사용할 수 있도록)
    // .accessTokenExpiry(): JwtConfig의 @ConfigurationProperties이 생성해준 메소드
    public  String generateAccessToken(User user) {
        return this.generateToken(user, jwtConfig.accessTokenExpiry());
    }

    // Refresh Token 생성하는 메소드(위 메소드가 프라이빗이므로 외부서 사용할 수 있도록)
    public  String generateRefreshToken(User user) {
        return this.generateToken(user, jwtConfig.refreshTokenExpiry());
    }
}
