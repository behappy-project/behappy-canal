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
import org.xiaowu.behappy.canal.client.client.SimpleCanalClient;
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

/**
 * @author xiaowu
 */
@Configuration(enforceUniqueMethods = false)
@EnableConfigurationProperties(CanalSimpleProperties.class)
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "simple", matchIfMissing = true)
@Import(ThreadPoolAutoConfiguration.class)
public class SimpleClientAutoConfiguration {

    private final CanalSimpleProperties canalSimpleProperties;

    public SimpleClientAutoConfiguration(CanalSimpleProperties canalSimpleProperties) {
        this.canalSimpleProperties = canalSimpleProperties;
    }

    @Bean
    public RowDataHandler<CanalEntry.RowData> rowDataHandler() {
        return new RowDataHandlerImpl(new EntryColumnModelFactory());
    }

    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
    public MessageHandler messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler, List<EntryHandler> entryHandlers,
                                         ExecutorService executorService) {
        return new AsyncMessageHandlerImpl(entryHandlers, rowDataHandler, executorService);
    }


    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "false")
    public MessageHandler messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler, List<EntryHandler> entryHandlers) {
        return new SyncMessageHandlerImpl(entryHandlers, rowDataHandler);
    }


    @Bean(initMethod = "start", destroyMethod = "stop")
    public SimpleCanalClient simpleCanalClient(MessageHandler messageHandler) {
        String server = canalSimpleProperties.getServer();
        String[] array = server.split(":");
        String destination = canalSimpleProperties.getDestination();
        String userName = canalSimpleProperties.getUserName();
        String password = canalSimpleProperties.getPassword();
        Integer batchSize = canalSimpleProperties.getBatchSize();
        String filter = canalSimpleProperties.getFilter();
        Long timeout = canalSimpleProperties.getTimeout();
        TimeUnit unit = canalSimpleProperties.getUnit();

        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress(array[0], Integer.parseInt(array[1])), destination, userName, password);
        SimpleCanalClient simpleCanalClient = new SimpleCanalClient();
        simpleCanalClient.setConnector(canalConnector);
        simpleCanalClient.setMessageHandler(messageHandler);
        simpleCanalClient.setFilter(filter);
        simpleCanalClient.setBatchSize(batchSize);
        simpleCanalClient.setTimeout(timeout);
        simpleCanalClient.setUnit(unit);
        return simpleCanalClient;
    }

}
