package org.xiaowu.behappy.canal.client.handler.impl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.xiaowu.behappy.canal.client.handler.AbstractMessageHandler;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.client.handler.RowDataHandler;

import java.util.List;

/**
 * @author xiaowu
 */
public class SyncMessageHandlerImpl extends AbstractMessageHandler {

    public SyncMessageHandlerImpl(List<? extends EntryHandler> entryHandlers, RowDataHandler<CanalEntry.RowData> rowDataHandler) {
        super(entryHandlers, rowDataHandler);
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
    }
}
