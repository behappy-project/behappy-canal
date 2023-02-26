package org.xiaowu.behappy.canal.client.factory;

import org.apache.commons.lang.StringUtils;
import org.xiaowu.behappy.canal.client.utils.EntryUtil;
import org.xiaowu.behappy.canal.client.utils.FieldUtil;

import java.util.Map;

/**
 * 获取原数据 Map类型
 * @author xiaowu
 */
public class MapColumnModelFactory extends AbstractModelFactory<Map<String, String>> {
    @Override
    <R> R newInstance(Class<R> c, Map<String, String> valueMap) throws Exception {
        R object = c.getDeclaredConstructor().newInstance();
        Map<String, String> columnNames = EntryUtil.getFieldName(object.getClass());
        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            String fieldName = columnNames.get(entry.getKey());
            if (StringUtils.isNotEmpty(fieldName)) {
                FieldUtil.setFieldValue(object, fieldName, entry.getValue());
            }
        }
        return object;
    }
}
