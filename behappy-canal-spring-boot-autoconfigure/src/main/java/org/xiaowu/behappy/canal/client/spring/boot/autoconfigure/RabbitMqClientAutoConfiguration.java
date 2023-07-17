package org.xiaowu.behappy.canal.client.spring.boot.autoconfigure;


import com.alibaba.otter.canal.client.rabbitmq.RabbitMQCanalConnector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.xiaowu.behappy.canal.client.client.RabbitMqCanalClient;
import org.xiaowu.behappy.canal.client.factory.MapColumnModelFactory;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;
import org.xiaowu.behappy.canal.client.handler.RowDataHandler;
import org.xiaowu.behappy.canal.client.handler.impl.AsyncFlatMessageHandlerImpl;
import org.xiaowu.behappy.canal.client.handler.impl.MapRowDataHandlerImpl;
import org.xiaowu.behappy.canal.client.handler.impl.SyncFlatMessageHandlerImpl;
import org.xiaowu.behappy.canal.client.spring.boot.properties.CanalProperties;
import org.xiaowu.behappy.canal.client.spring.boot.properties.CanalRabbitMqProperties;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaowu
 */
@Configuration(enforceUniqueMethods = false)
@EnableConfigurationProperties(CanalRabbitMqProperties.class)
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "rabbitMQ")
@Import(ThreadPoolAutoConfiguration.class)
public class RabbitMqClientAutoConfiguration {

    private final CanalRabbitMqProperties canalRabbitMqProperties;

    public RabbitMqClientAutoConfiguration(CanalRabbitMqProperties canalRabbitMqProperties) {
        this.canalRabbitMqProperties = canalRabbitMqProperties;
    }

    @Bean
    public RowDataHandler<List<Map<String, String>>> rowDataHandler() {
        return new MapRowDataHandlerImpl(new MapColumnModelFactory());
    }

    @DependsOn("messageHandler")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public RabbitMqCanalClient rabbitMqCanalClient(MessageHandler messageHandler) {
        String nameServer = canalRabbitMqProperties.getServer();
        String vhost = canalRabbitMqProperties.getVhost();
        String queueName = canalRabbitMqProperties.getDestination();
        String accessKey = canalRabbitMqProperties.getAccessKey();
        String secretKey = canalRabbitMqProperties.getSecretKey();
        String userName = canalRabbitMqProperties.getUserName();
        String password = canalRabbitMqProperties.getPassword();
        Long resourceOwnerId = canalRabbitMqProperties.getResourceOwnerId();
        String filter = canalRabbitMqProperties.getFilter();
        Long timeout = canalRabbitMqProperties.getTimeout();
        TimeUnit unit = canalRabbitMqProperties.getUnit();

        RabbitMQCanalConnector connector = new RabbitMQCanalConnector(nameServer, vhost, queueName, accessKey, secretKey, userName, password, resourceOwnerId, true);

        RabbitMqCanalClient rabbitMqCanalClient = new RabbitMqCanalClient();
        rabbitMqCanalClient.setConnector(connector);
        rabbitMqCanalClient.setMessageHandler(messageHandler);
        rabbitMqCanalClient.setFilter(filter);
        rabbitMqCanalClient.setTimeout(timeout);
        rabbitMqCanalClient.setUnit(unit);
        return rabbitMqCanalClient;
    }


    @Configuration
    @ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "rabbitMQ")
    @ConditionalOnExpression(value = "${canal.async:true}")
    public static class CanalAsyncMessageHandler{

        @Bean
        public MessageHandler messageHandler(RowDataHandler<List<Map<String, String>>> rowDataHandler,
                                             List<EntryHandler> entryHandlers,
                                             ExecutorService executorService) {
            return new AsyncFlatMessageHandlerImpl(entryHandlers, rowDataHandler, executorService);
        }
    }

    @Configuration
    @ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "rabbitMQ")
    @ConditionalOnExpression(value = "!${canal.async:true}")
    public static class CanalSyncMessageHandler{

        @Bean
        public MessageHandler messageHandler(RowDataHandler<List<Map<String, String>>> rowDataHandler, List<EntryHandler> entryHandlers) {
            return new SyncFlatMessageHandlerImpl(entryHandlers, rowDataHandler);
        }
    }
}
