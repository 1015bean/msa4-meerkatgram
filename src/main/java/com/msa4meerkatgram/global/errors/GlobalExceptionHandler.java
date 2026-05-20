package com.msa4meerkatgram.global.errors;

import com.msa4meerkatgram.global.responses.GlobalRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;

// 예외처리 커스텀
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // MethodArgumentTypeMismatchException: 1개의 요청 파라미터에 에러가 났을 때
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalRes<String>> methodArgumentTypeMismatchHandle(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(400).body(
                GlobalRes.<String>builder()
                        .code("E21")
                        .message("요청 파라미터에 문제가 있습니다.")
                        .data(String.format("%s : 필드를 확인해 주세요", e.getName()))
                        .build()
        );
    }

    // MethodArgumentTypeMismatchException: 여러개의 요청 파라미터에 에러가 났을 때
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalRes<List<String>>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(400).body(
                GlobalRes.<List<String>>builder()
                        .code("E21")
                        .message("요청 파라미터에 문제가 있습니다.")
                        .data(
                                // 여러개이므로 List로 에러들을 받음
                                // .map 이용하기 위해 스트림으로 변경, 이용 후 다시 리스트로 돌림
                                e.getBindingResult()
                                .getAllErrors()
                                .stream()
                                 // (item -> item.getDefaultMessage())
                             // .map(ObjectError::getDefaultMessage)
                                .map(item -> String.format("%s : 잘못된 값입니다.", item.getObjectName()))
                                .toList()
                        )
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalRes<String>> otherHandle(Exception e) {
        log.error(String.format(
                "시스템 에러: %s\n%s"
                ,e.getMessage()
                , Arrays.toString(e.getStackTrace())
                )
        );

        return ResponseEntity.status(500).body(
                GlobalRes.<String>builder()
                        .code("E99")
                        .message("시스템 에러")
                        .data("현재 서비스 이용이 불가합니다. 잠시후 다시 시도해 주십시오.")
                        .build()
        );
    }
}
