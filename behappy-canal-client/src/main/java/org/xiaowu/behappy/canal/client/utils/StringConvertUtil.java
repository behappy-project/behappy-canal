package org.xiaowu.behappy.canal.client.utils;

import com.alibaba.google.common.base.Enums;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author xiaowu
 */
@UtilityClass
public class StringConvertUtil {

    private  String[] PARSE_PATTERNS = new String[]{
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm", "yyyy.MM", "yyyy-MM-dd"};

     Object convertType(Class type, String columnValue) {
        if (columnValue == null || type.equals(String.class)) {
            return columnValue;
        } else if (type.equals(Integer.class)) {
            return columnValue.length() == 0 ? 0 : Integer.parseInt(columnValue);
        } else if (type.equals(Long.class)) {
            return columnValue.length() == 0 ? 0L : Long.parseLong(columnValue);
        } else if (type.equals(Boolean.class)) {
            return columnValue.length() == 0 ? Boolean.FALSE : convertToBoolean(columnValue);
        } else if (type.equals(BigDecimal.class)) {
            return columnValue.length() == 0 ? new BigDecimal(0) : new BigDecimal(columnValue);
        } else if (type.equals(Double.class)) {
            return columnValue.length() == 0 ? 0D : Double.parseDouble(columnValue);
        } else if (type.equals(Float.class)) {
            return columnValue.length() == 0 ? 0F : Float.parseFloat(columnValue);
        } else if (type.equals(Date.class)) {
            return parseDate(columnValue);
        } else if (type.equals(LocalDate.class)) {
            return parseLocalDate(columnValue);
        } else if (type.equals(LocalDateTime.class)) {
            return parseLocalDateTime(columnValue);
        } else if (type.equals(java.sql.Date.class)) {
            return parseSqlDate(columnValue);
        } else {
            // 枚举类型
            return Enums.stringConverter(type).convert(columnValue);
        }
    }

    private  Object parseLocalDateTime(String str) {
        if (str == null) {
            return null;
        } else {
            Instant instant = parseDate(str).toInstant();
            return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }

    private  Object parseLocalDate(String str) {
        if (str == null) {
            return null;
        } else {
            Instant instant = parseDate(str).toInstant();
            return instant.atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    private  Date parseDate(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                return DateUtils.parseDate(str, PARSE_PATTERNS);
            } catch (ParseException var2) {
                return null;
            }
        }
    }

    private  Date parseSqlDate(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                Date date = DateUtils.parseDate(str, PARSE_PATTERNS);
                return new java.sql.Date(date.getTime());
            } catch (ParseException var2) {
                return null;
            }
        }
    }

    private  boolean convertToBoolean(String value) {
        return "1".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
    }
}
