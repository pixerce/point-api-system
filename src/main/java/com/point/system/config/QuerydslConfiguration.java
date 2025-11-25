package com.point.system.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class QuerydslConfiguration {

    @PersistenceContext
    private final EntityManager entityManager; // EntityManager 주입

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        // JPAQueryFactory를 Bean으로 등록하여 쿼리 작성에 사용
        return new JPAQueryFactory(entityManager);
    }
}
