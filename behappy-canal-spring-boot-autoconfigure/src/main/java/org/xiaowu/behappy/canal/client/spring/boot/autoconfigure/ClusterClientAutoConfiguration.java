package org.xiaowu.behappy.canal.client.spring.boot.autoconfigure;


import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.xiaowu.behappy.canal.client.client.ClusterCanalClient;
import org.xiaowu.behappy.canal.client.factory.EntryColumnModelFactory;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;
import org.xiaowu.behappy.canal.client.handler.RowDataHandler;
import org.xiaowu.behappy.canal.client.handler.impl.AsyncMessageHandlerImpl;
import org.xiaowu.behappy.canal.client.handler.impl.RowDataHandlerImpl;
import org.xiaowu.behappy.canal.client.handler.impl.SyncMessageHandlerImpl;
import org.xiaowu.behappy.canal.client.spring.boot.properties.CanalProperties;
import org.xiaowu.behappy.canal.client.spring.boot.properties.CanalSimpleProperties;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableConfigurationProperties(CanalSimpleProperties.class)
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "cluster")
@Import(ThreadPoolAutoConfiguration.class)
public class ClusterClientAutoConfiguration {


    private final CanalSimpleProperties canalSimpleProperties;


    public ClusterClientAutoConfiguration(CanalSimpleProperties canalSimpleProperties) {
        this.canalSimpleProperties = canalSimpleProperties;
    }

    @Bean
    public RowDataHandler<CanalEntry.RowData> rowDataHandler() {
        return new RowDataHandlerImpl(new EntryColumnModelFactory());
    }

    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
    public MessageHandler<Message> messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler, List<EntryHandler<Message>> entryHandlers,
                                                  ExecutorService executorService) {
        return new AsyncMessageHandlerImpl(entryHandlers, rowDataHandler, executorService);
    }


    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "false")
    public MessageHandler<Message> messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler, List<EntryHandler<Message>> entryHandlers) {
        return new SyncMessageHandlerImpl(entryHandlers, rowDataHandler);
    }


    @Bean(initMethod = "start", destroyMethod = "stop")
    public ClusterCanalClient clusterCanalClient(MessageHandler<Message> messageHandler) {
        String canalServers = canalSimpleProperties.getServer();
        String destination = canalSimpleProperties.getDestination();
        String userName = canalSimpleProperties.getUserName();
        String password = canalSimpleProperties.getPassword();
        Integer batchSize = canalSimpleProperties.getBatchSize();
        String filter = canalSimpleProperties.getFilter();
        Long timeout = canalSimpleProperties.getTimeout();
        TimeUnit unit = canalSimpleProperties.getUnit();

        List<InetSocketAddress> serverList = Stream.of(canalServers.split(",")).map(s -> {
            String[] split = s.split(":");
            return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
        }).collect(Collectors.toList());
        CanalConnector connector = CanalConnectors.newClusterConnector(serverList, destination, userName, password);
        ClusterCanalClient clusterCanalClient = new ClusterCanalClient();
        clusterCanalClient.setMessageHandler(messageHandler);
        clusterCanalClient.setConnector(connector);
        clusterCanalClient.setFilter(filter);
        clusterCanalClient.setBatchSize(batchSize);
        clusterCanalClient.setTimeout(timeout);
        clusterCanalClient.setUnit(unit);
        return clusterCanalClient;
    }
}
