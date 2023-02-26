package org.xiaowu.behappy.canal.client.handler.impl;

import com.alibaba.otter.canal.protocol.FlatMessage;
import org.xiaowu.behappy.canal.client.handler.AbstractFlatMessageHandler;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.client.handler.RowDataHandler;

import java.util.List;
import java.util.Map;

/**
 * @author xiaowu
 */
public class SyncFlatMessageHandlerImpl extends AbstractFlatMessageHandler {



    public SyncFlatMessageHandlerImpl(List<? extends EntryHandler<?>> entryHandlers, RowDataHandler<List<Map<String, String>>> rowDataHandler) {
        super(entryHandlers, rowDataHandler);
    }

    @Override
    public void handleMessage(FlatMessage flatMessage) {
        super.handleMessage(flatMessage);
    }
}
