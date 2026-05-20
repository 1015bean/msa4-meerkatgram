package com.msa4meerkatgram.domain.post.entities;

import lombok.Builder;
import lombok.Getter;

// 6db에서 받아온 정보(맵퍼 실행 후)를 담을 엔티티객체
@Getter
@Builder
public class Post {
    private Long id;
    private Long userId;
    private String content;
    private String image;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}
