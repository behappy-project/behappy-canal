package org.xiaowu.behappy.canal.client.handler.impl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.extern.slf4j.Slf4j;
import org.xiaowu.behappy.canal.client.factory.IModelFactory;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.client.handler.RowDataHandler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 */
@Slf4j
public class RowDataHandlerImpl implements RowDataHandler<CanalEntry.RowData> {

    private final IModelFactory<List<CanalEntry.Column>> modelFactory;

    public RowDataHandlerImpl(IModelFactory<List<CanalEntry.Column>> modelFactory) {
        this.modelFactory = modelFactory;
    }

    @Override
    public <R> void handlerRowData(CanalEntry.RowData rowData, EntryHandler<R> entryHandler, CanalEntry.EventType eventType) throws Exception {
        if (entryHandler != null) {
            switch (eventType) {
                case INSERT -> {
                    R object = modelFactory.newInstance(entryHandler, rowData.getAfterColumnsList());
                    entryHandler.insert(object);
                }
                case UPDATE -> {
                    Set<String> updateColumnSet = rowData.getAfterColumnsList().stream().filter(CanalEntry.Column::getUpdated)
                            .map(CanalEntry.Column::getName).collect(Collectors.toSet());
                    R before = modelFactory.newInstance(entryHandler, rowData.getBeforeColumnsList(), updateColumnSet);
                    R after = modelFactory.newInstance(entryHandler, rowData.getAfterColumnsList());
                    entryHandler.update(before, after);
                }
                case DELETE -> {
                    R o = modelFactory.newInstance(entryHandler, rowData.getBeforeColumnsList());
                    entryHandler.delete(o);
                }
                default -> {
                    if (log.isDebugEnabled()) {
                        log.debug("未知消息类型 {} 不处理 {}", eventType, rowData);
                    }
                }
            }
        }
    }
}
