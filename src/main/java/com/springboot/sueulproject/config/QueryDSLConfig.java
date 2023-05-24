package com.springboot.sueulproject.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QueryDSLConfig {
    //PersistenceContext는 em을 Bean으로 주입할때 사용하는 어노테이션
    //Bean으로 주입할때 entitymanager는 autowired가 아닌 persistenceContext를 사용
    @PersistenceContext
    private EntityManager em;
    @Bean
    public JPAQueryFactory JPAQueryFactory(){
        return new JPAQueryFactory(em);
    }
}
