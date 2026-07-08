package com.msa4meerkatgram.domain.post.response;

import com.msa4meerkatgram.domain.post.entities.Post;

import java.util.List;

public record PostIndexRes(
        long total
        , boolean lastPage
        , List<PostWithUserRes> posts
) {
    public static PostIndexRes from(long total, boolean lastPage, List<Post> posts) {
        return  new PostIndexRes(
                total
                , lastPage
                , posts.stream().map(PostWithUserRes::from).toList()  // = .map(post -> PostWithUserRes.from(post))
                // > posts를 스트림화
                // > PostWithUserRes.from하여 만들어진 PostWith..객체들
                // >.map.toList 만들어진 객체들을 모아서 List화
        );
    }
}
