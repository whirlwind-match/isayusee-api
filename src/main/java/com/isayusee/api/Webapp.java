package com.isayusee.api;

import ch.qos.logback.classic.PatternLayout;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class Webapp extends AsyncConfigurerSupport {

    static {
        PatternLayout.defaultConverterMap.put(
                "user", LogbackUserConverter.class.getName());
    }

    public static void main(String[] args) {
        SpringApplication.run(Webapp.class, args);
    }

    @Override
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Spring Async - ");
        executor.initialize();
        return executor;
    }
}