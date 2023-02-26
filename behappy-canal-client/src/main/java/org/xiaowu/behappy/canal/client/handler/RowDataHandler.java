package org.xiaowu.behappy.canal.client.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;


/**
 * 元数据处理(Map和RowData)
 * @author xiaowu
 */
public interface RowDataHandler<T> {


    <R> void handlerRowData(T t, EntryHandler<R> entryHandler, CanalEntry.EventType eventType) throws Exception;
}
