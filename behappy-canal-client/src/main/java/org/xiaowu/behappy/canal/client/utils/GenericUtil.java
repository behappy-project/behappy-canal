package org.xiaowu.behappy.canal.client.utils;

import lombok.experimental.UtilityClass;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author xiaowu
 */
@UtilityClass
public class GenericUtil {

    private static Map<Class<? extends EntryHandler>, Class> cache = new ConcurrentHashMap<>();

    /**
     * 获取Table原对象
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    public <R> Class<R> getTableClass(EntryHandler<?> object) {
        Class<? extends EntryHandler> handlerClass = object.getClass();
        Class tableClass = cache.get(handlerClass);
        if (tableClass == null) {
            Type[] interfacesTypes = handlerClass.getGenericInterfaces();
            for (Type t : interfacesTypes) {
                Class<R> c = (Class<R>) ((ParameterizedType) t).getRawType();
                if (c.equals(EntryHandler.class)) {
                    tableClass = (Class<R>) ((ParameterizedType) t).getActualTypeArguments()[0];
                    cache.putIfAbsent(handlerClass, tableClass);
                    return tableClass;
                }
            }
        }
        return tableClass;
    }
}
