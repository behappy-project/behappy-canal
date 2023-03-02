package org.xiaowu.behappy.canal.client.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 字段赋值
 * @author xiaowu
 */
@UtilityClass
public class FieldUtil {

    public void setFieldValue(Object object,String fieldName,String value) throws NoSuchFieldException, IllegalAccessException {
        Field field;
        try{
            field = object.getClass().getDeclaredField(fieldName);
        }catch (NoSuchFieldException e){
            field = object.getClass().getSuperclass().getDeclaredField(fieldName);
        }
        field.setAccessible(true);
        Class type = field.getType();
        Object result = StringConvertUtil.convertType(type, value);
        field.set(object,result);
    }

    public List<Field> getAllFieldsList(Class cls) {
        notNull(cls, "The class must not be null", new Object[0]);
        List<Field> allFields = new ArrayList<>();
        for(Class currentClass = cls; currentClass != null; currentClass = currentClass.getSuperclass()) {
            Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
        }

        return allFields;
    }

    public <T> T notNull(T object, String message, Object... values) {
        return Objects.requireNonNull(object, () -> String.format(message, values));
    }
}
