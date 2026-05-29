package com.msa4meerkatgram.global.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

// @ConfigurationProperties: 특정 파일에 적어둔 설정값을 자바객체(이 클레스)에 넣어주는 어노테이션
@ConfigurationProperties(prefix = "security.jwt")
public record JwtConfig(
        boolean secure,
        String issuer,
        String type,
        int accessTokenExpiry,
        int refreshTokenExpiry,
        String refreshTokenCookieName,
        int refreshTokenCookieExpiry,
        String secret,
        String headerKey,
        String scheme,
        String reissUri
) {

}
