package org.xiaowu.behappy.canal.client.spring.boot.autoconfigure;


import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.xiaowu.behappy.canal.client.client.KafkaCanalClient;
import org.xiaowu.behappy.canal.client.factory.MapColumnModelFactory;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;
import org.xiaowu.behappy.canal.client.handler.RowDataHandler;
import org.xiaowu.behappy.canal.client.handler.impl.AsyncFlatMessageHandlerImpl;
import org.xiaowu.behappy.canal.client.handler.impl.MapRowDataHandlerImpl;
import org.xiaowu.behappy.canal.client.handler.impl.SyncFlatMessageHandlerImpl;
import org.xiaowu.behappy.canal.client.spring.boot.properties.CanalKafkaProperties;
import org.xiaowu.behappy.canal.client.spring.boot.properties.CanalProperties;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaowu
 */
@Configuration(enforceUniqueMethods = false)
@EnableConfigurationProperties(CanalKafkaProperties.class)
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "kafka")
@Import(ThreadPoolAutoConfiguration.class)
public class KafkaClientAutoConfiguration {

    private final CanalKafkaProperties canalKafkaProperties;

    public KafkaClientAutoConfiguration(CanalKafkaProperties canalKafkaProperties) {
        this.canalKafkaProperties = canalKafkaProperties;
    }

    @Bean
    public RowDataHandler<List<Map<String, String>>> rowDataHandler() {
        return new MapRowDataHandlerImpl(new MapColumnModelFactory());
    }

    @DependsOn("messageHandler")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public KafkaCanalClient kafkaCanalClient(MessageHandler messageHandler) {
        String server = canalKafkaProperties.getServer();
        String topic = canalKafkaProperties.getDestination();
        Integer partition = canalKafkaProperties.getPartition();
        String groupId = canalKafkaProperties.getGroupId();
        Integer batchSize = canalKafkaProperties.getBatchSize();
        String filter = canalKafkaProperties.getFilter();
        Long timeout = canalKafkaProperties.getTimeout();
        TimeUnit unit = canalKafkaProperties.getUnit();

        KafkaCanalConnector connector = new KafkaCanalConnector(server,topic, partition, groupId, batchSize, true);
        KafkaCanalClient kafkaCanalClient = new KafkaCanalClient();
        kafkaCanalClient.setConnector(connector);
        kafkaCanalClient.setMessageHandler(messageHandler);
        kafkaCanalClient.setFilter(filter);
        kafkaCanalClient.setTimeout(timeout);
        kafkaCanalClient.setUnit(unit);
        return kafkaCanalClient;
    }


    @Configuration
    @ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "kafka")
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
    @ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "kafka")
    @ConditionalOnExpression(value = "!${canal.async:true}")
    public static class CanalSyncMessageHandler{

        @Bean
        public MessageHandler messageHandler(RowDataHandler<List<Map<String, String>>> rowDataHandler, List<EntryHandler> entryHandlers) {
            return new SyncFlatMessageHandlerImpl(entryHandlers, rowDataHandler);
        }
    }
}
