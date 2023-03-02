package org.xiaowu.behappy.canal.client.factory;

import org.xiaowu.behappy.canal.client.constant.TableNameEnum;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.client.utils.GenericUtil;
import org.xiaowu.behappy.canal.client.utils.HandlerUtil;

/**
 * @author xiaowu
 */
public abstract class AbstractModelFactory<T> implements IModelFactory<T> {

    @Override
    public <R> R newInstance(EntryHandler entryHandler, T t) throws Exception {
        String canalTableName = HandlerUtil.getCanalTableName(entryHandler);
        if (TableNameEnum.ALL.name().toLowerCase().equals(canalTableName)) {
            return (R) t;
        }
        Class<R> tableClass = GenericUtil.getTableClass(entryHandler);
        if (tableClass != null) {
            return newInstance(tableClass, t);
        }
        return null;
    }

    abstract <R> R newInstance(Class<R> c, T t) throws Exception;
}
