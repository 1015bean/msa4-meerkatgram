package com.msa4meerkatgram.domain.post.entities;

import com.msa4meerkatgram.domain.user.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posts")
@SQLDelete(sql = "UPDATE posts SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    // @현재 엔티티 수 & 연결할 엔티티 수: 테이블 간 관계(다대일, 다대다, 등등)
    // (fetch = FetchType. ... ): join하는 데이터 가져올 방식, EAGER or LAZY, 디폴트는 LAZY
        // EAGER: join된 모든 테이블의 데이터 다 가져옴
        // LAZY: 본 테이블 데이터만 가져오고 join된 테이블은 필요할 때 가져옴
    @ManyToOne(fetch = FetchType.LAZY)
    // 테이블 끼리 join할 때, join해서 가져올 테이블에 대한 설정
    @JoinColumn(
            name = "user_id"
            , insertable = true
            , updatable = false
            , nullable = false
            , foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))  // 물리적fk 설정하고 싶지 않을 때
    private User user;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Column(name = "image", nullable = false, length = 100)
    private String image;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;
}
