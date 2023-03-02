package org.xiaowu.behappy.canal.client.client;

import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaowu
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class KafkaCanalClient extends AbstractCanalClient {
    @Override
    public void process() {
        KafkaCanalConnector connector = (KafkaCanalConnector) getConnector();
        MessageHandler messageHandler = getMessageHandler();
        while (flag) {
            try {
                connector.connect();
                connector.subscribe();
                while (flag) {
                    try {
                        List<FlatMessage> messages = connector.getFlatListWithoutAck(timeout, unit);
                        log.info("获取消息 {}", messages);
                        if (messages != null) {
                            for (FlatMessage flatMessage : messages) {
                                messageHandler.handleMessage(flatMessage);
                            }
                        }
                        connector.ack();
                    } catch (Exception e) {
                        log.error("canal 消费异常", e);
                    }
                }
            } catch (Exception e) {
                log.error("canal 连接异常", e);
            }
        }
        connector.unsubscribe();
        connector.disconnect();
    }

    private String filter = StringUtils.EMPTY;
    private Integer batchSize = 1;
    private Long timeout = 1L;
    private TimeUnit unit = TimeUnit.SECONDS;
}
