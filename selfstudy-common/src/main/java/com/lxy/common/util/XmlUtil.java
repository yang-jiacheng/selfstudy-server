package com.lxy.common.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/**
 * xml 工具类
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2026/1/30 16:59
 */

@Slf4j
public class XmlUtil {

    private static final XmlMapper xmlMapper;

    static {
        xmlMapper = new XmlMapper();
        xmlMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        xmlMapper.setDateFormat(new SimpleDateFormat(DateCusUtil.YYYY_MM_DD_HH_MM_SS));
        // 序列化空值失败时不抛异常
        xmlMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 反序列化不存在的字段失败时不抛异常
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T getTypeObj(String xml, Class<T> clazz) {
        if (StrUtil.isEmpty(xml)) {
            return null;
        } else {
            try {
                return xmlMapper.readValue(xml, clazz);
            } catch (Exception var3) {
                log.error("json to type error", var3);
                return null;
            }
        }
    }

    public static <T> T getObj(String xml, TypeReference<T> typeReference) {
        if (StrUtil.isEmpty(xml)) {
            return null;
        }
        try {
            // Map<String, Object> map = XmlUtil.getObj(detailJson,new TypeReference<HashMap<String,Object>>(){});
            return xmlMapper.readValue(xml, typeReference);
        } catch (Exception e) {
            log.error("JSON to TypeReference<{}> error: {}", typeReference.getType().getTypeName(), e.getMessage());
            return null;
        }
    }

    public static String toXml(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return xmlMapper.writer().withRootName("xml").writeValueAsString(object);
        } catch (Exception e) {
            log.error("Object 转换为 XML 失败", e);
            return null;
        }
    }

}
