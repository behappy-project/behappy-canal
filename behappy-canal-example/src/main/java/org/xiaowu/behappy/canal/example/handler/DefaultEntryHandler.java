package org.xiaowu.behappy.canal.example.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.canal.client.annotation.CanalTable;
import org.xiaowu.behappy.canal.client.context.CanalContext;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;

import java.util.Map;

/**
 * 获取到map 对象后转换成sql，使用jooq执行 sql
 */
@CanalTable(value = "all")
@Component
@Slf4j
public class DefaultEntryHandler implements EntryHandler<Map<String, String>> {

    @Override
    public void insert(Map<String, String> map) {
        log.info("增加 {}", map);
        String table = CanalContext.getModel().getTable();
        log.info("table {}", table);
    }

    @Override
    public void update(Map<String, String> before, Map<String, String> after) {
        log.info("修改 before {}", before);
        log.info("修改 after {}", after);
        String table = CanalContext.getModel().getTable();
        log.info("table {}", table);
    }

    @Override
    public void delete(Map<String, String> map) {
        log.info("删除 {}", map);
        String table = CanalContext.getModel().getTable();
        log.info("table {}", table);
    }
}
