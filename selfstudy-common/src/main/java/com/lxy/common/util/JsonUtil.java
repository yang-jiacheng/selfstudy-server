package com.lxy.common.util;


import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * @author jiacheng yang.
 */

@Slf4j
public class JsonUtil {


    private static final ObjectMapper objectMapper = createObjectMapper();

    private JsonUtil() {
    }

    public static ObjectMapper createObjectMapper() {
        // 解决查询缓存转换异常的问题
        ObjectMapper objectMapper = new ObjectMapper();
        //所有属性可见 允许任何可见性的属性被序列化和反序列化
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 启用默认类型处理，使得在序列化和反序列化时能够处理多态类型。
        objectMapper.activateDefaultTyping(
                //允许任意子类型
                LaissezFaireSubTypeValidator.instance,
                //对非 final 类型的属性进行类型信息处理
                ObjectMapper.DefaultTyping.NON_FINAL,
                //将类型信息作为属性添加到 JSON 中
                JsonTypeInfo.As.PROPERTY);

        objectMapper.setDateFormat(new SimpleDateFormat(DateCusUtil.YYYY_MM_DD_HH_MM_SS));
        // 序列化空 Bean时不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 反序列化时忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    public static <TYPE> TYPE getTypeObj(String json, Class<TYPE> type) {
        if (StrUtil.isEmpty(json)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(json, type);
            } catch (Exception var3) {
                log.error("json to type error", var3);
                return null;
            }
        }
    }

    public static <T> T getObj(String json, TypeReference<T> typeReference) {
        if (StrUtil.isEmpty(json)) {
            return null;
        }
        try {
            //Map<Integer, QuestionResultVO> answers = JsonUtils.getObj(detailJson,new TypeReference<HashMap<Integer,QuestionResultVO>>(){});
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            log.error("JSON to TypeReference<{}> error: {}", typeReference.getType().getTypeName(), e.getMessage());
            return null;
        }
    }


    public static <TYPE> List<TYPE> getListType(String json, Class<TYPE> type) {
        if (StrUtil.isEmpty(json)) {
            return null;
        }
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, type);
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.error("JSON to List<{}> error: {}", type.getSimpleName(), e.getMessage());
            return null;
        }
    }

    public static <TYPE> Set<TYPE> getSetType(String json, Class<TYPE> type) {
        if (StrUtil.isEmpty(json)) {
            return null;
        }
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Set.class, type);
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.error("JSON to Set<{}> error: {}", type.getSimpleName(), e.getMessage());
            return null;
        }
    }

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        } else {
            String json = null;

            try {
                json = objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException var3) {
                log.error("json to string error", var3);
            }

            return json;
        }
    }

}
