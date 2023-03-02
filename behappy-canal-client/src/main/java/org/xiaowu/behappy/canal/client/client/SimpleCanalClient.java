package org.xiaowu.behappy.canal.client.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;



/**
 * @author xiaowu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleCanalClient extends AbstractCanalClient {

    private String filter = StringUtils.EMPTY;
    private Integer batchSize = 1;
    private Long timeout = 1L;
    private TimeUnit unit = TimeUnit.SECONDS;

}
