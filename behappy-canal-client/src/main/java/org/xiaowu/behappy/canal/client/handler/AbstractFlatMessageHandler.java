package org.xiaowu.behappy.canal.client.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.FlatMessage;
import lombok.extern.slf4j.Slf4j;
import org.xiaowu.behappy.canal.client.context.CanalContext;
import org.xiaowu.behappy.canal.client.model.CanalModel;
import org.xiaowu.behappy.canal.client.utils.HandlerUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 处理FlatMessage
 * @author xiaowu
 */
@Slf4j
public abstract class AbstractFlatMessageHandler implements MessageHandler<FlatMessage> {

    private final Map<String, EntryHandler> tableHandlerMap;

    private final RowDataHandler<List<Map<String, String>>> rowDataHandler;

    public AbstractFlatMessageHandler(List<? extends EntryHandler> entryHandlers, RowDataHandler<List<Map<String, String>>> rowDataHandler) {
        this.tableHandlerMap = HandlerUtil.getTableHandlerMap(entryHandlers);
        this.rowDataHandler = rowDataHandler;
    }

    @Override
    public void handleMessage(FlatMessage flatMessage) {
        if (log.isDebugEnabled()) {
            log.debug("解析消息 {}", flatMessage);
        }
        List<Map<String, String>> data = flatMessage.getData();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                CanalEntry.EventType eventType = CanalEntry.EventType.valueOf(flatMessage.getType());
                List<Map<String, String>> maps;
                if (eventType.equals(CanalEntry.EventType.UPDATE)) {
                    Map<String, String> map = data.get(i);
                    Map<String, String> oldMap = flatMessage.getOld().get(i);
                    maps = Stream.of(map, oldMap).collect(Collectors.toList());
                } else {
                    maps = Stream.of(data.get(i)).collect(Collectors.toList());
                }
                try {
                    EntryHandler entryHandler = HandlerUtil.getEntryHandler(tableHandlerMap, flatMessage.getTable());
                    if (log.isDebugEnabled()) {
                        log.debug("消息处理器 {}", entryHandler);
                    }
                    if (entryHandler != null) {
                        CanalModel model = CanalModel.builder().id(flatMessage.getId()).table(flatMessage.getTable())
                                .executeTime(flatMessage.getEs()).database(flatMessage.getDatabase()).createTime(flatMessage.getTs()).build();
                        CanalContext.setModel(model);
                        if (log.isDebugEnabled()) {
                            log.debug("消息发送至行处理 {} {}", maps, eventType);
                        }
                        rowDataHandler.handlerRowData(maps, entryHandler, eventType);
                    }
                } catch (Exception e) {
                    log.error("消息处理异常 ", e);
                    throw new RuntimeException("parse event has an error , data:" + data.toString(), e);
                } finally {
                    CanalContext.removeModel();
                }
            }
        }
    }
}
