package org.xiaowu.behappy.canal.client.utils;



import lombok.experimental.UtilityClass;
import org.xiaowu.behappy.canal.client.annotation.CanalTable;
import org.xiaowu.behappy.canal.client.constant.TableNameEnum;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaowu
 */
@UtilityClass
public class HandlerUtil {

    public EntryHandler<?> getEntryHandler(List<? extends EntryHandler<?>> entryHandlers, String tableName) {
        EntryHandler<?> globalHandler = null;
        for (EntryHandler<?> handler : entryHandlers) {
            String canalTableName = getCanalTableName(handler);
            if (TableNameEnum.ALL.name().toLowerCase().equals(canalTableName)) {
                globalHandler = handler;
                continue;
            }
            if (tableName.equals(canalTableName)) {
                return handler;
            }
        }
        return globalHandler;
    }

    public Map<String, EntryHandler<?>> getTableHandlerMap(List<? extends EntryHandler<?>> entryHandlers) {
        Map<String, EntryHandler<?>> map = new ConcurrentHashMap<>();
        if (entryHandlers != null && entryHandlers.size() > 0) {
            for (EntryHandler<?> handler : entryHandlers) {
                String canalTableName = getCanalTableName(handler);
                // 必须标注@CanalTable
                if (canalTableName != null) {
                    map.putIfAbsent(canalTableName.toLowerCase(), handler);
                }
            }
        }
        return map;
    }

    public EntryHandler<?> getEntryHandler(Map<String, EntryHandler<?>> map, String tableName) {
        EntryHandler<?> entryHandler = map.get(tableName);
        if (entryHandler == null) {
            return map.get(TableNameEnum.ALL.name().toLowerCase());
        }
        return entryHandler;
    }

    public String getCanalTableName(EntryHandler<?> entryHandler) {
        CanalTable canalTable = entryHandler.getClass().getAnnotation(CanalTable.class);
        if (canalTable != null) {
            return canalTable.value();
        }
        return null;
    }
}
