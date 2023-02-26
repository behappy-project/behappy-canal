package org.xiaowu.behappy.canal.client.spring.boot.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = CanalSimpleProperties.CANAL_PREFIX)
public class CanalKafkaProperties extends CanalProperties {

    private Integer partition;

    private String groupId;
}
