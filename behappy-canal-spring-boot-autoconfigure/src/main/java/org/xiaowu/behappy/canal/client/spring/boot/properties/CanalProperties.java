package org.xiaowu.behappy.canal.client.spring.boot.properties;


import lombok.Data;

import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * @author xiaowu
 */
@Data
public class CanalProperties {

    public static final String CANAL_PREFIX = "canal";

    public static final String CANAL_ASYNC = CANAL_PREFIX + "." + "async";

    public static final String CANAL_MODE = CANAL_PREFIX + "." + "mode";

    /**
     * simple,cluster,zookeeper,kafka,rocketMQ,rabbitMQ
     */
    private String mode;

    private Boolean async;

    private String server;

    private String destination;

    private String filter = EMPTY;

    private Integer batchSize = 1;

    private Long timeout = 1L;

    private TimeUnit unit = TimeUnit.SECONDS;

}
