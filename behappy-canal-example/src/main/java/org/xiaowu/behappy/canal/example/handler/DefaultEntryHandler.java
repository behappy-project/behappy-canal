package org.xiaowu.behappy.canal.example.handler;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.canal.client.annotation.CanalTable;
import org.xiaowu.behappy.canal.client.context.CanalContext;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 * 获取到map 对象后转换成sql，使用jooq执行 sql
 * @author yang peng
 * @date 2019/4/1915:19
 */
@CanalTable(value = "all")
@Component
@Slf4j
public class DefaultEntryHandler implements EntryHandler<Map<String, String>> {


    @Resource
    private DSLContext dsl;

    @Override
    public void insert(Map<String, String> map) {
        log.info("增加 {}", map);
        String table = CanalContext.getModel().getTable();
        List<Field<Object>> fields = map.keySet().stream().map(DSL::field).collect(Collectors.toList());
        List<Param<String>> values = map.values().stream().map(DSL::value).collect(Collectors.toList());
        int execute = dsl.insertInto(table(table)).columns(fields).values(values).execute();
        log.info("执行结果 {}", execute);
    }

    @Override
    public void update(Map<String, String> before, Map<String, String> after) {
        log.info("修改 before {}", before);
        log.info("修改 after {}", after);
        String table = CanalContext.getModel().getTable();
        Map<Field<Object>, String> map = after.entrySet().stream().filter(entry -> before.get(entry.getKey()) != null)
                .collect(Collectors.toMap(entry -> field(entry.getKey()), Map.Entry::getValue));
        dsl.update(table(table)).set(map).where(field("id").eq(after.get("id"))).execute();
    }

    @Override
    public void delete(Map<String, String> map) {
        log.info("删除 {}", map);
        String table = CanalContext.getModel().getTable();
        dsl.delete(table(table)).where(field("id").eq(map.get("id"))).execute();
    }
}
