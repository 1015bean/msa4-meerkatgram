package com.msa4meerkatgram.domain.post.mapper;

import com.msa4meerkatgram.domain.post.entities.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// 5DB랑 연결해줄 맵퍼 작성
// Service에서 이용할 메소드 작성
@Mapper
public interface PostMapper {
    // 데이터 가져오는 메소드
    // 데이터타입: 엔티티 객체(DB에서 받아온 정보 담을 DTO객체)에 데이터 담기
    List<Post> getPagination(int limit, int offset);

    long getTotal();
    Post findByPk(long id);
    long countPostsByUserId(long userId);
}