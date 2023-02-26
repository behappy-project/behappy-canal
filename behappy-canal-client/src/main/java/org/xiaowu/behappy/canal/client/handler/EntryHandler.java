package org.xiaowu.behappy.canal.client.handler;

/**
 * 数据处理
 * @author xiaowu
 */
public interface EntryHandler<T> {



    default void insert(T t) {

    }


    default void update(T before, T after) {

    }


    default void delete(T t) {

    }
}
