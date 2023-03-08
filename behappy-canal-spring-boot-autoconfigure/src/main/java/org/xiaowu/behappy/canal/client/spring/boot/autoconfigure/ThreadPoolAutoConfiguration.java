package org.xiaowu.behappy.canal.client.spring.boot.autoconfigure;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xiaowu.behappy.canal.client.handler.CanalThreadUncaughtExceptionHandler;
import org.xiaowu.behappy.canal.client.spring.boot.properties.CanalProperties;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 小五
 */
@Configuration
@ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
public class ThreadPoolAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService executorService() {
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("canal-execute-thread-%d")
                .uncaughtExceptionHandler(new CanalThreadUncaughtExceptionHandler()).build();
        return Executors.newFixedThreadPool(20, factory);
    }
}
