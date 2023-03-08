package org.xiaowu.behappy.canal.client.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.google.common.base.CaseFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
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
            List<Field> fields = distinct(FieldUtils.getAllFieldsList(c));
            //如果实体类中存在column 注解，则使用column注解的名称为字段名
            map = fields.stream().filter(EntryUtil::notTransient)
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toMap(EntryUtil::getColumnName, Field::getName));
            cache.putIfAbsent(c, map);
        }
        return map;
    }


    /**
     * 去重
     * @param fields  fields
     * @return fields
     */
    private static List<Field> distinct(List<Field> fields){
        return fields.stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(EntryUtil::getColumnName))),
                                ArrayList::new
                        )
                );
    }

    /**
     * @param field
     * @return
     */
    private static String getColumnName(Field field) {
        // jpa
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column) && org.apache.commons.lang3.StringUtils.isNotEmpty(column.name())) {
            return column.name();
        }
        // mp
        TableField tableField = field.getAnnotation(TableField.class);
        if (Objects.nonNull(tableField) && org.apache.commons.lang3.StringUtils.isNotEmpty(tableField.value())) {
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
