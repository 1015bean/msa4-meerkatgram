package com.msa4meerkatgram.domain.post.response;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.msa4meerkatgram.domain.user.responses.UserRes;

import java.time.LocalDateTime;

public record PostWithUserRes(
        Long id
        , String content
        , String image
        , LocalDateTime createAt
        , LocalDateTime updateAt
        , LocalDateTime deleteAt
        , UserRes userRes
) {
    public static PostWithUserRes from(Post post) {
        return new PostWithUserRes(
                post.getId()
                , post.getContent()
                , post.getImage()
                , post.getCreatedAt()
                , post.getUpdatedAt()
                , post.getDeletedAt()
                , UserRes.from(post.getUser())
        );
    }
}
