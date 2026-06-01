package com.msa4meerkatgram.global.security.filter;

import com.msa4meerkatgram.global.config.CorsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CorsConfig corsConfig;

    // @Bean: 스프링 프레임워크가 제공하는 파일(클레스)에서는 클래스레벨에서 @Component 사용X
    // , 대신 메소드에 @Bean 등록

    // 비번 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 크로스도메인 허용 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 프론트엔드 도메인 설정
        configuration.setAllowedOrigins(corsConfig.allowedOrigins());

        // 허용할 프론트엔드 도메인 설정
        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name()
                ,HttpMethod.POST.name()
                ,HttpMethod.PUT.name()
                ,HttpMethod.PATCH.name()
                ,HttpMethod.DELETE.name()
                ,HttpMethod.OPTIONS.name() // preflight(요청 보내기 전 서버를 확인해보는 예비요청-보안) 요청 허용
        ));

        // 허용할 헤더 지정
        // 토큰이 담기는 부분, 요청하는 데이터의 형식, 요청하는 본문(body)에 사용되는 부분
        configuration.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION
                ,HttpHeaders.CONTENT_TYPE
                ,HttpHeaders.ACCEPT
        ));

        // 자격증명(Cookie, 인증헤더 정보 등등) 포함 여부 설정
        configuration.setAllowCredentials(true);

        // 브라우저가 preflight 요청 결과를 캐시할(가지고 있을) 시간(초 단위) 설정
        configuration.setMaxAge(corsConfig.maxAge());

        // 모든 API경로에 위 설정을 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // 세션 비활성화 설정: 세큐리티는 기본 세션방식 이용하므로 세션방식 이용 off
    // throws: 예외처리. 예외발생시 외부로 던져줌
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, SecurityExceptionHandler securityExceptionHandler, TokenAuthenticationFilter tokenAuthenticationFilter) throws Exception{
        return http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable) // 화면 설정 비활성 설정
                .formLogin(AbstractHttpConfigurer::disable) // 폼로그인 기능 비활성 설정
                .csrf(AbstractHttpConfigurer::disable) // csrf 토큰인증 비활성 설정
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource())) // CORS 설정(크로스도메인 허용) 추가
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 작선한 필터 등록(내가 만든 jwt필터 먼저 실행, 기본제공 필터 실행)
                .authorizeHttpRequests(req ->
                        // 리퀘스트에 대한 권한 설정(이 패턴의 url(블랙리스트)은 인증이 필요하다)
                        req.requestMatchers(HttpMethod.GET, SecurityUrlRegistry.AUTH_REQUIRED_GET_URLS).authenticated()
                                .requestMatchers(HttpMethod.POST, SecurityUrlRegistry.AUTH_REQUIRED_POST_URLS).authenticated()
                                .requestMatchers(HttpMethod.PUT, SecurityUrlRegistry.AUTH_REQUIRED_PUT_URLS).authenticated()
                                .requestMatchers(HttpMethod.PATCH, SecurityUrlRegistry.AUTH_REQUIRED_PATCH_URLS).authenticated()
                                .requestMatchers(HttpMethod.DELETE, SecurityUrlRegistry.AUTH_REQUIRED_DELETE_URLS).authenticated()
                                .anyRequest().permitAll() // 그 외는 인증 불필요
                )
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(securityExceptionHandler)
                        .accessDeniedHandler(securityExceptionHandler)
                )
                .build();

    }
}
