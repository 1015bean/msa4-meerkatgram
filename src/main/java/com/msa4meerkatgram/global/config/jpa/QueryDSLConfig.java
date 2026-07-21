package com.msa4meerkatgram.global.config.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDSLConfig {

    // @PersistenceContext: JPA에서 DB와 상호작용하기위한 객체 EntityManager를 spring컨텍스트에 자동 주입(JPA가 사용할 수 있도록 등록)
    @PersistenceContext
    private EntityManager entityManager;
    // entityManager
    // entity의 영속성 관리를 담당하는 JPA의 핵심 인터페이스(JPA와 DB를 연결)
    // CRUD작업, 커리 실행 등 DB와의 상호작용을 담당

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        // JPAQueryFactory: QueryDSL를 사용하기 위해 필요한 객체
        return new JPAQueryFactory(entityManager);
    }
}
