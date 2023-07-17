package org.xiaowu.behappy.canal.client.spring.boot.autoconfigure;


import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.xiaowu.behappy.canal.client.client.ZookeeperClusterCanalClient;
import org.xiaowu.behappy.canal.client.factory.EntryColumnModelFactory;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;
import org.xiaowu.behappy.canal.client.handler.RowDataHandler;
import org.xiaowu.behappy.canal.client.handler.impl.AsyncMessageHandlerImpl;
import org.xiaowu.behappy.canal.client.handler.impl.RowDataHandlerImpl;
import org.xiaowu.behappy.canal.client.handler.impl.SyncMessageHandlerImpl;
import org.xiaowu.behappy.canal.client.spring.boot.properties.CanalProperties;
import org.xiaowu.behappy.canal.client.spring.boot.properties.CanalSimpleProperties;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaowu
 */
@Configuration(enforceUniqueMethods = false)
@EnableConfigurationProperties(CanalSimpleProperties.class)
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "zk")
@Import(ThreadPoolAutoConfiguration.class)
public class ZookeeperClientAutoConfiguration {

    private final CanalSimpleProperties canalSimpleProperties;

    public ZookeeperClientAutoConfiguration(CanalSimpleProperties canalSimpleProperties) {
        this.canalSimpleProperties = canalSimpleProperties;
    }

    @Bean
    public RowDataHandler<CanalEntry.RowData> rowDataHandler() {
        return new RowDataHandlerImpl(new EntryColumnModelFactory());
    }


    @DependsOn("messageHandler")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public ZookeeperClusterCanalClient zookeeperClusterCanalClient(MessageHandler messageHandler) {
        String zkServers = canalSimpleProperties.getServer();
        String destination = canalSimpleProperties.getDestination();
        String userName = canalSimpleProperties.getUserName();
        String password = canalSimpleProperties.getPassword();
        Integer batchSize = canalSimpleProperties.getBatchSize();
        String filter = canalSimpleProperties.getFilter();
        Long timeout = canalSimpleProperties.getTimeout();
        TimeUnit unit = canalSimpleProperties.getUnit();

        CanalConnector connector = CanalConnectors.newClusterConnector(zkServers, destination, userName, password);
        ZookeeperClusterCanalClient zookeeperClusterCanalClient = new ZookeeperClusterCanalClient();
        zookeeperClusterCanalClient.setMessageHandler(messageHandler);
        zookeeperClusterCanalClient.setConnector(connector);
        zookeeperClusterCanalClient.setBatchSize(batchSize);
        zookeeperClusterCanalClient.setFilter(filter);
        zookeeperClusterCanalClient.setTimeout(timeout);
        zookeeperClusterCanalClient.setUnit(unit);
        return zookeeperClusterCanalClient;
    }

    @Configuration
    @ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "zk")
    @ConditionalOnExpression(value = "${canal.async:true}")
    public static class CanalAsyncMessageHandler{
        @Bean
        public MessageHandler messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler, List<EntryHandler> entryHandlers,
                                             ExecutorService executorService) {
            return new AsyncMessageHandlerImpl(entryHandlers, rowDataHandler, executorService);
        }
    }

    @Configuration
    @ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "zk")
    @ConditionalOnExpression(value = "!${canal.async:true}")
    public static class CanalSyncMessageHandler{

        @Bean
        public MessageHandler messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler, List<EntryHandler> entryHandlers) {
            return new SyncMessageHandlerImpl(entryHandlers, rowDataHandler);
        }
    }

}
