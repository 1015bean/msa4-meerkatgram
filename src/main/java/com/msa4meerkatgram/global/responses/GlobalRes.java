package com.msa4meerkatgram.global.responses;

import lombok.Builder;
import lombok.Getter;

// 3유저에게 return할 형식을 지정: 레스폰스 객체
@Getter
@Builder
public class GlobalRes<T> {
    private String code;
    private String message;
    private T data;
}
