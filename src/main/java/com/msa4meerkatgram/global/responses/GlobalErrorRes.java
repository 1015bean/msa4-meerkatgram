package com.msa4meerkatgram.global.responses;

// 3유저에게 return할 형식을 지정: 레스폰스 객체
public record GlobalErrorRes(
        String code
        , String message
) {
    public static GlobalErrorRes from(String code, String message) {
        return new GlobalErrorRes(code, message);
    }
}
