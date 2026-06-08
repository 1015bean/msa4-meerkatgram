package com.msa4meerkatgram.domain.post.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostReq(
        @NotBlank(message = "게시글은 필수입니다.")
        @Size(max = 500, message = "글자수 제한 500자 이하.")
        String content,

        @NotBlank(message = "이미지 링크가 입력되지 않았습니다.")
        // @Pattern(
        //         regexp = "^/files/posts/.+$",
        //         message = "올바르지 않는 이미지 경로입니다."
        // )
        String image
) {
}
