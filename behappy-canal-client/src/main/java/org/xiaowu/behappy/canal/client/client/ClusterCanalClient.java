package org.xiaowu.behappy.canal.client.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.xiaowu.behappy.canal.client.handler.MessageHandler;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiaowu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClusterCanalClient extends AbstractCanalClient {
}
