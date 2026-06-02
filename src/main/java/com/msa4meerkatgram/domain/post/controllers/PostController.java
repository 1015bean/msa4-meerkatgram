package com.msa4meerkatgram.domain.post.controllers;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.msa4meerkatgram.domain.post.requests.PostIndexReq;
import com.msa4meerkatgram.domain.post.requests.PostIndexRes;
import com.msa4meerkatgram.domain.post.services.PostService;
import com.msa4meerkatgram.global.responses.GlobalRes;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// posts: 요청받은 게시물 정보(page) 가져와서 보여줌 - 1유저요청 받는 컨트롤러 만들기
// 메소드(리퀘스트DTO의 형식대로 요청 받음)
    // !다시 보기! 데이터 전송 방식
    // form 데이터: 여러 개의 정보, 외부에 노출 차단(HTTP의 Body에 데이터 저장)
    // 세그먼트 파라미터: url의 path를 데이터 변수로 사용. 많은 데이터에는 부적합
    // JSON: 대량의 복잡한 데이터
    // 쿼리 파라미터: URL에 데이터 노출
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<GlobalRes<PostIndexRes>> index(PostIndexReq postIndexReq) {
        // Service에서 작성한 서비스메소드의 데이터를 객체에 담기
        PostIndexRes postIndexRes = postService.index(postIndexReq);


        // 레스폰스 엔티티의 body에 "미리 만들어둔 레스폰스 객체를 담아" 반환하도록 함
        return ResponseEntity.status(200).body(
                GlobalRes.<PostIndexRes>builder()
                        .code("00")
                        .message("정상처리")
                        .data(postIndexRes)
                        .build()
        );

        // 요청DTO 객체와 매핑 잘 됐는지 점검
        // return String.format("page: %d, limit: %d", req.page(), req.limit());
    }

    // @PathVariable: URL Path의 일부를 {변수}로 서버에서 추출해서 사용
    @GetMapping("/posts/{id}")
    public ResponseEntity<GlobalRes<Post>> show(
            @Min(value = 1, message = "1 이상 숫자만 허용합니다.") @PathVariable long id
    ) {
        Post result = postService.show(id);

        return ResponseEntity.status(200).body(
                GlobalRes.<Post>builder()
                        .code("00")
                        .message("게시글 상세 정상 처리")
                        .data(result)
                        .build()
        );
    }
}