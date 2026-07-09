package com.msa4meerkatgram.global.errors;

import com.msa4meerkatgram.global.errors.constant.CustomErrorCode;
import com.msa4meerkatgram.global.errors.custom.*;
import com.msa4meerkatgram.global.responses.GlobalErrorRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

// 예외처리 커스텀
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<GlobalErrorRes> generateErrorResponse(CustomErrorCode customErrorCode) {
        return ResponseEntity.status(customErrorCode.getHttpStatus())
                .body(GlobalErrorRes.from(customErrorCode.getCode(), customErrorCode.name()));
    }

    // NotRegisteredException: 로그인 에러(커스텀 에러)
    @ExceptionHandler(NotRegisteredException.class)
    public ResponseEntity<GlobalErrorRes> notRegisteredHandle(NotRegisteredException e) {
        log.debug(CustomErrorCode.NOT_REGISTERED_ERROR.name(), e);
        return this.generateErrorResponse(CustomErrorCode.NOT_REGISTERED_ERROR);
    }

    // AuthenticationException: 인증 에러(커스텀 에러)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GlobalErrorRes> authenticationHandle(AuthenticationException e) {
        log.debug(CustomErrorCode.UNAUTHENTICATED_ERROR.name(), e);
        return this.generateErrorResponse(CustomErrorCode.UNAUTHENTICATED_ERROR);
    }

    // AccessDeniedException: 권한 에러(커스텀 에러)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GlobalErrorRes> accessDeniedHandle(AccessDeniedException e) {
        log.debug(CustomErrorCode.UNAUTHORIZED_ERROR.name(), e);
        return this.generateErrorResponse(CustomErrorCode.UNAUTHORIZED_ERROR);
    }

    // InvalidTokenException: 토큰 에러(커스텀 에러)
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<GlobalErrorRes> InvalidTokenHandle(InvalidTokenException e) {
        log.debug(CustomErrorCode.INVALID_TOKEN_ERROR.name(), e);
        return this.generateErrorResponse(CustomErrorCode.INVALID_TOKEN_ERROR);
    }

    // InvalidTokenException: 토큰 에러(커스텀 에러)
    @ExceptionHandler(DeletedRecordException.class)
    public ResponseEntity<GlobalErrorRes> deletedRecordHandle(DeletedRecordException e) {
        log.debug(CustomErrorCode.NOT_FOUND_DATA_ERROR.name(), e);
        return this.generateErrorResponse(CustomErrorCode.NOT_FOUND_DATA_ERROR);
    }

    // InvalidTokenException: 토큰 에러(커스텀 에러)
    @ExceptionHandler(DuplicatedRecordException.class)
    public ResponseEntity<GlobalErrorRes> duplicatedRecordHandle(DuplicatedRecordException e) {
        log.debug(CustomErrorCode.DUPLICATED_DATA_ERROR.name(), e);
        return this.generateErrorResponse(CustomErrorCode.DUPLICATED_DATA_ERROR);
    }

    // MethodArgumentTypeMismatchException: 1개의 요청 파라미터에 에러가 났을 때
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalErrorRes> methodArgumentTypeMismatchHandle(MethodArgumentTypeMismatchException e) {
        log.debug(CustomErrorCode.INVALID_PARAMETER_ERROR.name(), String.format("%s : 필드를 확인해 주세요", e.getName()));
        return this.generateErrorResponse(CustomErrorCode.INVALID_PARAMETER_ERROR);
    }

    // MethodArgumentTypeMismatchException: 여러개의 요청 파라미터에 에러가 났을 때
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorRes> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField, // 필드명
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "유효하지 않은 값입니다.",
                        (existing, replacement) -> existing // 중복 필드가 있을 경우 기존 값 유지
                ));

        log.debug(CustomErrorCode.INVALID_PARAMETER_ERROR.name(), errors);
        return this.generateErrorResponse(CustomErrorCode.INVALID_PARAMETER_ERROR);
    }

    // FileManagedException: 파일 업로드 에러(커스텀 에러)
    @ExceptionHandler(FileManagedException.class)
    public ResponseEntity<GlobalErrorRes> FileManagedHandle(FileManagedException e) {
        log.debug(CustomErrorCode.FILE_MANEGED_ERROR.name(), e);
        return this.generateErrorResponse(CustomErrorCode.FILE_MANEGED_ERROR);
    }


    @ExceptionHandler(SQLException.class)
    public ResponseEntity<GlobalErrorRes> SQLHandle(Exception e) {
        log.error("DB 에러", e);
        return this.generateErrorResponse(CustomErrorCode.DB_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalErrorRes> otherHandle(Exception e) {
        log.error("시스템 에러", e);
        return this.generateErrorResponse(CustomErrorCode.SYSTEM_ERROR);
    }
}
