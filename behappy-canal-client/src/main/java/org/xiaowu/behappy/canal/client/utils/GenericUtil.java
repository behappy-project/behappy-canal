package org.xiaowu.behappy.canal.client.utils;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.CaseFormat;
import jakarta.persistence.Table;
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


    static String getTableGenericProperties(EntryHandler entryHandler) {
        Class<?> tableClass = getTableClass(entryHandler);
        if (tableClass != null) {
            // jpa
            Table table = tableClass.getAnnotation(Table.class);
            if (table != null) {
                return table.name();
            }
            // mp
            TableName tableName = tableClass.getAnnotation(TableName.class);
            if (tableName != null) {
                return tableName.value();
            }
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tableClass.getSimpleName());
        }
        return null;
    }

    /**
     * 获取Table原对象
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getTableClass(EntryHandler object) {
        Class<? extends EntryHandler> handlerClass = object.getClass();
        Class tableClass = cache.get(handlerClass);
        if (tableClass == null) {
            Type[] interfacesTypes = handlerClass.getGenericInterfaces();
            for (Type t : interfacesTypes) {
                Class c = (Class) ((ParameterizedType) t).getRawType();
                if (c.equals(EntryHandler.class)) {
                    tableClass = (Class<T>) ((ParameterizedType) t).getActualTypeArguments()[0];
                    cache.putIfAbsent(handlerClass, tableClass);
                    return tableClass;
                }
            }
        }
        return tableClass;
    }
}
