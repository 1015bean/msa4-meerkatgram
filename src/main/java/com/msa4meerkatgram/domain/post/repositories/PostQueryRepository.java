package com.msa4meerkatgram.domain.post.repositories;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

// Q-class(쿼리에서 엔티티를 다룰 때 사용되는 객체)의 특정 필드 임포트
// (컴파일 시, 엔티티객체와 같은 곳에 자동 생성될 예정)
import static com.msa4meerkatgram.domain.post.entities.QPost.post;
import static com.msa4meerkatgram.domain.user.entities.QUser.user;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // 페이지네이션 쿼리 평문
    // SELECT *
    // FROM posts
    //  JOIN users
    //      on posts.user_id = users.id
    // WHERE deleted_at IS NULL > Post엔티티(JPA)에서 delete설정 해줬으므로 여기서 안써도 됨(jpaQueryFactory에 엔티티매니저 담겨있으므로 JPA엔티티의 설정값 적용됨)
    // ORDER BY created_at DESC, id ASC
    // LIMIT ? OFFSET ?

    // post.user: 큐클래스 Post의 user 필드(User user)
    // .join( on절, join할 테이블 )
    // .fetchJoin(): 데이터를 가져올 때 연관된 데이터까지 한 방에(하나의 SQL 쿼리로) 다 가져오기
    // .fetch(): 쿼리 결과 반환하기
    public List<Post> pagination(int offset, int limit) {

        return  jpaQueryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .orderBy(post.createdAt.desc(), post.id.desc())
                .limit(limit)
                .offset(offset)
                .fetch();
    }
}
