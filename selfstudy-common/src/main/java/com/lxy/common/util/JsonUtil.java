package com.lxy.common.util;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lxy.common.po.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jiacheng yang.
 */
public class JsonUtil {

	private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final ObjectMapper objectMapper;

	static{
		objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
		// 序列化空值失败时不抛异常
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 反序列化不存在的字段失败时不抛异常
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public JsonUtil() {
	}

	public static <TYPE> TYPE getTypeObj(String json, Class<TYPE> type) {
		if (StrUtil.isEmpty(json)) {
			return null;
		} else {
			try {
				return objectMapper.readValue(json, type);
			} catch (Exception var3) {
				logger.error("json to type error", var3);
				return null;
			}
		}
	}

//	public static <TYPE> TYPE getObj(String json, TypeReference<?> typeReference) {
//		if (StrUtil.isEmpty(json)) {
//			return null;
//		} else {
//			try {
//				//Map<Integer, QuestionResultVO> answers = JsonUtils.getObj(detailJson,new TypeReference<HashMap<Integer,QuestionResultVO>>(){});
//				return (TYPE)objectMapper.readValue(json, typeReference);
//			} catch (Exception e) {
//				logger.error("json to type error", e);
//				return null;
//			}
//		}
//	}

	public static <T> T getObj(String json, TypeReference<T> typeReference) {
		if (StrUtil.isEmpty(json)) {
			return null;
		}
		try {
			//Map<Integer, QuestionResultVO> answers = JsonUtils.getObj(detailJson,new TypeReference<HashMap<Integer,QuestionResultVO>>(){});
			return objectMapper.readValue(json, typeReference);
		} catch (Exception e) {
			logger.error("JSON to TypeReference<{}> error: {}", typeReference.getType().getTypeName(), e.getMessage());
			return null;
		}
	}

	public static void main(String[] args) {

	}

	public static <TYPE> List<TYPE> getListType(String json, Class<TYPE> type) {
		if (StrUtil.isEmpty(json)) {
			return null;
		}
		try {
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, type);
			return objectMapper.readValue(json, javaType);
		} catch (Exception e) {
			logger.error("JSON to List<{}> error: {}", type.getSimpleName(), e.getMessage());
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
			logger.error("JSON to Set<{}> error: {}", type.getSimpleName(), e.getMessage());
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
				logger.error("json to string error", var3);
			}

			return json;
		}
	}

}
