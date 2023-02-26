package org.xiaowu.behappy.canal.client.factory;

import org.xiaowu.behappy.canal.client.handler.EntryHandler;

import java.util.Set;

/**
 * @author xiaowu
 */
public interface IModelFactory<T> {
    <R> R newInstance(EntryHandler<?> entryHandler, T t) throws Exception;

    default <R> R newInstance(EntryHandler<?> entryHandler, T t, Set<String> updateColumn) throws Exception {
        return null;
    }
}
