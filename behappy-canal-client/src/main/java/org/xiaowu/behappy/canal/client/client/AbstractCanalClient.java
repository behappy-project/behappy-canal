package org.xiaowu.behappy.canal.client.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaowu
 */
@Slf4j
@Setter
@Getter
public abstract class AbstractCanalClient implements CanalClient {
    protected volatile boolean flag;

    private Thread workThread;

    private String filter = StringUtils.EMPTY;

    private Integer batchSize = 1;

    private Long timeout = 1L;

    private TimeUnit unit = TimeUnit.SECONDS;

    private MessageHandler messageHandler;

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
                // 打开连接
                connector.connect();
                // 订阅数据库表，来覆盖服务端初始化时的设置
                connector.subscribe(filter);
                // 回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
                connector.rollback();
                while (flag) {
                    // 获取指定数量的数据
                    Message message = connector.getWithoutAck(batchSize, timeout, unit);
                    long batchId = message.getId();
                    if (message.getId() != -1 && message.getEntries().size() != 0) {
                        if (log.isDebugEnabled()) {
                            log.debug("获取消息 {}", message);
                        }
                        messageHandler.handleMessage(message);
                    }
                    // 进行 batch id 的确认
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
