package com.msa4meerkatgram.domain.user.controllers;

import com.msa4meerkatgram.domain.auth.responses.AuthRes;
import com.msa4meerkatgram.domain.user.services.UserService;
import com.msa4meerkatgram.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService uSerService;

    @GetMapping("/test")
    public ResponseEntity<GlobalRes<AuthRes>> test() {
        return ResponseEntity.status(200).body(
                GlobalRes.<AuthRes>builder()
                        .code("00")
                        .data(uSerService.test())
                        .message("정상처리")
                        .build()
        );
    }
}
