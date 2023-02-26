package org.xiaowu.behappy.canal.client.handler.impl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.extern.slf4j.Slf4j;
import org.xiaowu.behappy.canal.client.factory.IModelFactory;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.client.handler.RowDataHandler;

import java.util.List;
import java.util.Map;

/**
 * @author xiaowu
 */
@Slf4j
public class MapRowDataHandlerImpl implements RowDataHandler<List<Map<String, String>>> {

    private final IModelFactory<Map<String, String>> modelFactory;

    public MapRowDataHandlerImpl(IModelFactory<Map<String, String>> modelFactory) {
        this.modelFactory = modelFactory;
    }

    @Override
    public <R> void handlerRowData(List<Map<String, String>> list, EntryHandler<R> entryHandler, CanalEntry.EventType eventType) throws Exception {
        if (entryHandler != null) {
            log.info("处理消息 {}", list);
            switch (eventType) {
                case INSERT -> {
                    R entry = modelFactory.newInstance(entryHandler, list.get(0));
                    entryHandler.insert(entry);
                }
                case UPDATE -> {
                    R before = modelFactory.newInstance(entryHandler, list.get(1));
                    R after = modelFactory.newInstance(entryHandler, list.get(0));
                    entryHandler.update(before, after);
                }
                case DELETE -> {
                    R o = modelFactory.newInstance(entryHandler, list.get(0));
                    entryHandler.delete(o);
                }
                default -> log.info("未知消息类型 {} 不处理 {}", eventType, list);
            }
        }
    }
}
