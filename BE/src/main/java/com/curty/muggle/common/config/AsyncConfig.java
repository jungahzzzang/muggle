package com.curty.muggle.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig { // 비동기 트랜잭션 안에서의 스레드 수를 관리하기 위한 설정 파일
    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(2);       // 기본 스레드 수 (코어 개수, 동시 처리 가능한 작업 수)
        executor.setMaxPoolSize(6);        // 최대 스레드 수 (최대 스레드 수)
        executor.setQueueCapacity(100);    // 작업 큐 용량 (corePoolSize를 초과하는 작업들이 대기하는 큐)
        executor.initialize();             // 스레드 풀 초기화
        return executor;
    }
}