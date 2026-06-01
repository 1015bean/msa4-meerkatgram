package com.msa4meerkatgram.global.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

// @Qualifier: 스프링부트 bean에 있는 메소드. 스프링 수하에 있는 동명이메소드 중 이친구를 불러오겠다
@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private final HandlerExceptionResolver handlerExceptionResolver;

    public SecurityExceptionHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    // (받아온 인터페이스)의 추상메소드 오버라이드
    // 401(미인증) 관련: (리퀘스트객체, 리시폰스객체, 전달해줄 예외처리, 예외객체)
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
            handlerExceptionResolver.resolveException(request, response, null, authException);
    }

    // 403(권한) 관련: (리퀘스트객체, 리시폰스객체, 전달해줄 예외처리, 예외객체)
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
    }


}
