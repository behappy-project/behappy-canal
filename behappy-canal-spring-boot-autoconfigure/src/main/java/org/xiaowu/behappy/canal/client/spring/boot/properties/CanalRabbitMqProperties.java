package org.xiaowu.behappy.canal.client.spring.boot.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * @author xiaowu
 */
@Data
@ConfigurationProperties(prefix = CanalSimpleProperties.CANAL_PREFIX)
public class CanalRabbitMqProperties extends CanalSimpleProperties {

    private String vhost = "/";

    private String accessKey = EMPTY;

    private String secretKey = EMPTY;

    /**
     * 账户uid
     */
    private Long resourceOwnerId;
}
