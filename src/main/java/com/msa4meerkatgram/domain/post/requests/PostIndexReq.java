package com.msa4meerkatgram.domain.post.requests;

import jakarta.validation.constraints.Min;

// 2리퀘스트 받고 컨트롤러 실행 시 요청정보 담을 "DTO객체"
// 데이터의 불변성 유지 & 메소드 자동생성 되므로 레코드로 작성
    // (spring)DTO역할1: 리퀘스트 데이터를 받아서 여기저기 전달해주는 역할(컨트롤러, 서비스 등에 전달)
    // DTO역할2: 데이터의 유효성 검사
public record PostIndexReq(
        // 담을 데이터 양식 지정, 유효성 검사
        @Min(value = 1, message = "1 이상 숫자만 허용합니다.")
        Integer page,

        @Min(value = 1, message = "1 이상 숫자만 허용합니다.")
        Integer limit
) {
    // 생성자 초기값 설정(잘못된 요청이 왔을 시 이 값으로 지정)
    public PostIndexReq(Integer page, Integer limit) {
        this.page = (page != null && page > 0) ? page : 1;
        this.limit = (limit != null && limit > 0) ? limit : 6;
    }
}
