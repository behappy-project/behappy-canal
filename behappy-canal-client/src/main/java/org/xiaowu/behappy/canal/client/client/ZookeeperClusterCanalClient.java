package org.xiaowu.behappy.canal.client.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;

import java.util.concurrent.TimeUnit;
/**
 * @author xiaowu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ZookeeperClusterCanalClient extends AbstractCanalClient {
}
