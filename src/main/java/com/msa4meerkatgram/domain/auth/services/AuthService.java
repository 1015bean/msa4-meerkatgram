package com.msa4meerkatgram.domain.auth.services;

import com.msa4meerkatgram.domain.auth.repositories.AuthRepository;
import com.msa4meerkatgram.domain.auth.requests.LoginReq;
import com.msa4meerkatgram.domain.auth.requests.RegistrationReq;
import com.msa4meerkatgram.domain.auth.responses.AuthRes;
import com.msa4meerkatgram.domain.post.repositories.PostRepository;
import com.msa4meerkatgram.domain.user.entities.User;
import com.msa4meerkatgram.global.errors.custom.DuplicatedRecordException;
import com.msa4meerkatgram.global.errors.custom.InvalidTokenException;
import com.msa4meerkatgram.global.errors.custom.NotRegisteredException;
import com.msa4meerkatgram.global.security.constant.ProviderPolicy;
import com.msa4meerkatgram.global.security.constant.RolePolicy;
import com.msa4meerkatgram.global.security.cookie.CookieManager;
import com.msa4meerkatgram.global.security.jwt.JwtConfig;
import com.msa4meerkatgram.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final CookieManager cookieManager;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    private final PostRepository postRepository;

    // `로그인 로직`
    @Transactional(rollbackFor = Exception.class)
    public AuthRes login(HttpServletResponse response, LoginReq loginReq) {

// ---------------------------------------------- Mybatis ----------------------------------------------------------------
        // 유저정보 획득
//        User user = userMapper.findByEmail(loginReq.email());

        // 유저 가입여부 확인
            // (user가 없을 시) 에러 발생시킴
//        if(user == null) {
//            throw new NotRegisteredException("아이디와 비밀번호를 확인해 주세요.");
//        }
// ---------------------------------------------- Mybatis ----------------------------------------------------------------


// ---------------------------------------------- JPA ----------------------------------------------------------------
        // 유저정보 획득 & 유저 가입여부 확인
        User user = authRepository.findByEmail(loginReq.email())
                .orElseThrow(() -> new NotRegisteredException("아이디와 비밀번호를 확인해 주세요."));
// ---------------------------------------------- JPA ----------------------------------------------------------------


        // 비밀번호 체크
        if(!passwordEncoder.matches(loginReq.password(), user.getPassword())) {
            throw new NotRegisteredException("아이디와 비밀번호를 확인해 주세요.");
        }

        // 엑세스토큰&리프레시토큰 생성 후, 리프레시토큰 DB&Cookie 저장, AuthRes로 반환
        return this.generateAuthentication(response, user);
    }

    // `엑세스토큰 재발급`
    @Transactional(rollbackFor = Exception.class)
    public AuthRes reissue(HttpServletRequest request, HttpServletResponse response) {

        // 리프레시토큰 획득 & 없을경우 예외처리
//        Optional<String> refreshTokenOptional = jwtProvider.extractRefreshToken(request);
//        if(refreshTokenOptional.isEmpty()) {
//            throw new InvalidTokenException("토큰이 없습니다.");
//        }
//        String extractRefreshToken = refreshTokenOptional.get();

        String extractRefreshToken = jwtProvider.extractRefreshToken(request)
                .orElseThrow(()-> new InvalidTokenException("토큰이 없습니다."));

        // .extractClaims(): 획득한 토큰 검증 & 클레임(유저정보) 추출
        // .getSubject(): 값 중에 핵심값(id 등) 추출
        long id = Long.parseLong(jwtProvider.extractClaims(extractRefreshToken).getSubject());

// ---------------------------------------------- Mybatis ----------------------------------------------------------------
//        // 유저 획득
//        User user = userMapper.findByPk(id);
//
//        // 유저 가입여부 확인
//        if(user == null || user.getRefreshToken() == null) {
//            throw new InvalidTokenException("유효하지 않은 회원의 토큰입니다.");
//        }
// ---------------------------------------------- Mybatis ----------------------------------------------------------------

// ---------------------------------------------- JPA ----------------------------------------------------------------
        User user = authRepository.findById(id)
                .orElseThrow(() -> new InvalidTokenException("유효하지 않은 회원의 토큰입니다."));
// ---------------------------------------------- JPA ----------------------------------------------------------------

        // 리프레시토큰 비교
        if(!user.getRefreshToken().equals(extractRefreshToken)) {
            throw new InvalidTokenException("토큰이 일치하지 않습니다.");
        }

        // 엑세스토큰&리프레시토큰 생성 후, 리프레시토큰 DB&Cookie 저장, AuthRes로 반환
        return this.generateAuthentication(response, user);
    }

    /**
     * 엑세스토큰&리프레시토큰 생성 후, 리프레시토큰 DB&Cookie 저장, AuthRes로 반환
     * @param response
     * @param user
     * @return
     */
    private AuthRes generateAuthentication(HttpServletResponse response, User user) {
        // 작성 게시글 수 획득
        long countPosts = postRepository.countByUser(user);

        // 토큰 생성
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

// ---------------------------------------------- Mybatis ----------------------------------------------------------------
        // 리프레시 토큰을 서버(DB)에 저장
//      authMapper.updateRefreshToken(user.getId(), newRefreshToken);
// ---------------------------------------------- Mybatis ----------------------------------------------------------------

// ---------------------------------------------- JPA ---------------------------------------------------------------
        // 리프레시 토큰을 서버(DB)에 저장
        user.setRefreshToken(newRefreshToken);
        authRepository.save(user);  // save(): 리프레시토큰 변경한 user데이터를 DB에 업데이트
// ---------------------------------------------- JPA ---------------------------------------------------------------

        // 리프레시 토큰을 쿠키에 저장
        cookieManager.setCookie(response, jwtConfig.refreshTokenCookieName(), newRefreshToken, jwtConfig.refreshTokenCookieExpiry(), jwtConfig.reissUri());

// ---------------------------------------------- Mybatis ----------------------------------------------------------------
        // 리턴
//        return AuthRes.builder()
//                .accessToken(newAccessToken)
//                .user(
//                        UserRes.builder()
//                                .id(user.getId())
//                                .email(user.getEmail())
//                                .nick(user.getNick())
//                                .role(user.getRole())
//                                .profile(user.getProfile())
//                                .createdAt(user.getCreatedAt())
//                                .countPosts(countPosts)
//                                .build()
//                )
//                .build();
// ---------------------------------------------- Mybatis ----------------------------------------------------------------

// ---------------------------------------------- JPA ----------------------------------------------------------------
        // 리턴
        return AuthRes.from(user, newAccessToken, countPosts);
// ---------------------------------------------- JPA ----------------------------------------------------------------
    }

    //로그아웃 로직
    @Transactional(rollbackFor = Exception.class)
    public void logout(HttpServletResponse response, long id) {
        // 유저 정보 획득
        User user = authRepository.findById(id)
                .orElseThrow(() -> new InvalidTokenException("유효하지 않은 회원의 토큰입니다."));

        // DB에 저장한 리프레시토큰 파기
        user.setRefreshToken(null);
        authRepository.save(user);

        // Cookie에 저장한 리프레시토큰 파기
        cookieManager.setCookie(
                response
                ,jwtConfig.refreshTokenCookieName()
                ,null
                ,0
                ,jwtConfig.reissUri()
        );
    }

    // 회원가입 로직
    @Transactional(rollbackFor = Exception.class)
    public void registration(RegistrationReq registrationReq) {

// ---------------------------------------------- Mybatis ----------------------------------------------------------------
//        // 중복유저 방지(유저 정보 획득)
//        User user = userMapper.findByEmail(registrationReq.email());
//
//        if (user != null) {
//            throw new DuplicatedRecordException("이미 가입된 회원입니다.");
//        }
// ---------------------------------------------- Mybatis ----------------------------------------------------------------

// ---------------------------------------------- JPA ----------------------------------------------------------------
        // 유저 가입여부 확인
        if (authRepository.existsByEmail(registrationReq.email())) {
            throw new DuplicatedRecordException("이미 가입된 회원입니다.");
        }
// ---------------------------------------------- JPA ----------------------------------------------------------------

        User newUser = new User();
        newUser.setEmail(registrationReq.email());
        newUser.setPassword(passwordEncoder.encode(registrationReq.password()));
        newUser.setNick(registrationReq.nick());
        newUser.setProfile(registrationReq.profile());
        newUser.setProvider(ProviderPolicy.NONE);
        newUser.setRole(RolePolicy.NORMAL);
        authRepository.save(newUser);
    }
}
