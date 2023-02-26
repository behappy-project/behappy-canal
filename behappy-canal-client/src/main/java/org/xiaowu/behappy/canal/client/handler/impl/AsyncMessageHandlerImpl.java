package org.xiaowu.behappy.canal.client.handler.impl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.xiaowu.behappy.canal.client.handler.AbstractMessageHandler;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.client.handler.RowDataHandler;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author xiaowu
 */
public class AsyncMessageHandlerImpl extends AbstractMessageHandler {

    private final ExecutorService executor;

    public AsyncMessageHandlerImpl(List<? extends EntryHandler<Message>> entryHandlers, RowDataHandler<CanalEntry.RowData> rowDataHandler, ExecutorService executor) {
        super(entryHandlers, rowDataHandler);
        this.executor = executor;
    }

    @Override
    public void handleMessage(Message message) {
        executor.execute(() -> super.handleMessage(message));
    }
}
