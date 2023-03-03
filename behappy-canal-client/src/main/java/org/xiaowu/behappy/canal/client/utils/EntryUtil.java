package org.xiaowu.behappy.canal.client.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.google.common.base.CaseFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 */
@UtilityClass
public class EntryUtil {

    private static Map<Class, Map<String, String>> cache = new ConcurrentHashMap<>();

    /**
     * 获取字段名称和实体属性的对应关系
     *
     * @param c class
     * @return map
     */
    public static Map<String, String> getFieldName(Class c) {
        Map<String, String> map = cache.get(c);
        if (map == null) {
            List<Field> fields = FieldUtil.getAllFieldsList(c);
            //如果实体类中存在column 注解，则使用column注解的名称为字段名
            map = fields.stream().filter(EntryUtil::notTransient)
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toMap(EntryUtil::getColumnName, Field::getName));
            cache.putIfAbsent(c, map);
        }
        return map;
    }


    /**
     * issue
     * @param field
     * @return
     */
    private static String getColumnName(Field field) {
        // jpa
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            return column.name();
        }
        // mp
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null) {
            return tableField.value();
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
    }

    private static boolean notTransient(Field field) {
        // mp
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null) {
            return tableField.exist();
        }
        // jpa
        return field.getAnnotation(Transient.class) == null;
    }


}
