package com.msa4meerkatgram.global.responses;

// 3유저에게 return할 형식을 지정: 레스폰스 객체
public record GlobalRes<T>(
        String code
        , String message
        , T data
) {
    public static <T> GlobalRes<T> from(String code, String message, T data) {
        return new GlobalRes<T>(code, message, data);
    }
}
