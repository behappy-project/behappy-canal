package org.xiaowu.behappy.canal.client.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

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

}
