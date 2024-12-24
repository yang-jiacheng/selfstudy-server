package com.lxy.common.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.hutool.core.util.StrUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jiacheng yang.
 */
public class JsonUtil {
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static ObjectMapper objectMapper;
	public static Map<String, Integer> answerMap;

	static{
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setDateFormat( new SimpleDateFormat(DATE_FORMAT));
	}

	public JsonUtil() {
	}

	public static <TYPE> List<TYPE> getListType(String json, Class<TYPE> type) {
		if (StrUtil.isEmpty(json)) {
			return null;
		} else {
			try {
				List<TYPE> list = (List)objectMapper.readValue(json, objectMapper.getTypeFactory().constructParametricType(ArrayList.class, new Class[]{type}));
				return list;
			} catch (Exception var4) {
				var4.printStackTrace();
				return null;
			}
		}
	}

	public static <TYPE> TYPE getTypeObj(String json, Class<TYPE> type) {
		if (StrUtil.isEmpty(json)) {
			return null;
		} else {
			try {
				return objectMapper.readValue(json, type);
			} catch (Exception var3) {
				var3.printStackTrace();
				return null;
			}
		}
	}

	public static <TYPE> TYPE getObj(String json, TypeReference<?> typeReference) {
		if (StrUtil.isEmpty(json)) {
			return null;
		} else {
			try {
				return (TYPE) objectMapper.readValue(json, typeReference);
			} catch (Exception var3) {
				var3.printStackTrace();
				return null;
			}
		}
	}

	public static <TYPE> TYPE getObj(String json, Class<TYPE> type) {
		if (StrUtil.isEmpty(json)) {
			return null;
		} else {
			try {
				JavaType javaType = getCollectionType(type);
				return objectMapper.readValue(json, javaType);
			} catch (Exception var3) {
				var3.printStackTrace();
				return null;
			}
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
				var3.printStackTrace();
			}

			return json;
		}
	}

	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	public static <TYPE> Set<TYPE> getSetType(String json, Class<TYPE> type) {
		if (StrUtil.isEmpty(json)) {
			return null;
		} else {
			try {
				Set<TYPE> set = (Set)objectMapper.readValue(json, objectMapper.getTypeFactory().constructParametricType(LinkedHashSet.class, new Class[]{type}));
				return set;
			} catch (Exception var4) {
				var4.printStackTrace();
				return null;
			}
		}
	}

	public static Map<String, Object> toMap(Object object) {
		Map map = null;

		try {
			String json = objectMapper.writeValueAsString(object);
			map = (Map)objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception var3) {
			var3.printStackTrace();
		}

		return map;
	}
}
