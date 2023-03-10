package org.xiaowu.behappy.canal.client.client;

import com.alibaba.otter.canal.client.rabbitmq.RabbitMQCanalConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;

import java.util.List;

/**
 * @author xiaowu
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class RabbitMqCanalClient extends AbstractCanalClient {

    @Override
    public void process() {
        RabbitMQCanalConnector connector = (RabbitMQCanalConnector) getConnector();
        MessageHandler messageHandler = getMessageHandler();
        while (flag) {
            try {
                // 打开连接
                connector.connect();
                // 订阅数据库表，来覆盖服务端初始化时的设置
                connector.subscribe(getFilter());
                // 回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
                connector.rollback();
                while (flag) {
                    try {
                        List<FlatMessage> messages = connector.getFlatListWithoutAck(getTimeout(), getUnit());
                        if (messages != null) {
                            if (log.isDebugEnabled()) {
                                log.debug("获取消息 {}", messages);
                            }
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

}
