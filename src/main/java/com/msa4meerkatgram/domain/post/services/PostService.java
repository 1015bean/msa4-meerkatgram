package com.msa4meerkatgram.domain.post.services;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.msa4meerkatgram.domain.post.mapper.PostMapper;
import com.msa4meerkatgram.domain.post.requests.PostIndexReq;
import com.msa4meerkatgram.domain.post.requests.PostIndexRes;
import com.msa4meerkatgram.global.errors.custom.DeletedRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 4게시물 정보 가져와서 보여주는 서비스 만들기
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostMapper postMapper;

    public PostIndexRes index(PostIndexReq postIndexReq) {

        // 특정 페이지의 게시글 조회: 맵퍼(쿼리문 작성)가 필요한 정보를 전달해줌
        // 필요한 정보: page & offset
        int offset = (postIndexReq.page() - 1) * postIndexReq.limit();
        List<Post> posts = postMapper.getPagination(postIndexReq.limit(), offset);

        // 토탈(모든 데이터) 획득
        long total = postMapper.getTotal();
        boolean lastPage = offset + postIndexReq.limit() >= total;

        // 위에서 획득한 데이터 컨트롤러에게 전달
        return PostIndexRes.builder()
                .total(total)
                .lastPage(lastPage)
                .posts(posts)
                .build();
    }

    public Post show(long id) {
        Post post = postMapper.findByPk(id);

        if(post == null) {
            throw new DeletedRecordException("이미 삭제한 게시글입니다.");
        }

        return post;
    }
}
