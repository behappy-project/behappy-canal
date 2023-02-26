package org.xiaowu.behappy.canal.client.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaowu
 */
@Slf4j
public abstract class AbstractCanalClient<T> implements CanalClient<T> {
    protected volatile boolean flag;

    private Thread workThread;
    protected String filter = StringUtils.EMPTY;
    protected Integer batchSize = 1;
    protected Long timeout = 1L;
    protected TimeUnit unit = TimeUnit.SECONDS;

    @Getter
    @Setter
    private MessageHandler<T> messageHandler;

    @Getter
    @Setter
    private CanalConnector connector;

    @Override
    public void start() {
        log.info("start canal client");
        workThread = new Thread(this::process);
        workThread.setName("canal-client-thread");
        flag = true;
        workThread.start();
    }

    @Override
    public void stop() {
        log.info("stop canal client");
        flag = false;
        if (null != workThread) {
            workThread.interrupt();
        }

    }

    @Override
    public void process() {
        while (flag) {
            try {
                connector.connect();
                connector.subscribe(filter);
                while (flag) {
                    Message message = connector.getWithoutAck(batchSize, timeout, unit);
                    log.info("获取消息 {}", message);
                    long batchId = message.getId();
                    if (message.getId() != -1 && message.getEntries().size() != 0) {
                        messageHandler.handleMessage((T) message);
                    }
                    connector.ack(batchId);
                }
            } catch (Exception e) {
                log.error("canal client 异常", e);
            } finally {
                connector.disconnect();
            }
        }
    }
}
