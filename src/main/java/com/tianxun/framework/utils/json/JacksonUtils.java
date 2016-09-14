package com.tianxun.framework.utils.json;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tianxun.framework.utils.sys.SystemHelper;
/**
 * 使用Jackson解析json
 * 业务根据自己需要可以加入一些自定义方法
 * 在使用上注意和SpringMVC中的解析器规则保持一致
 * @author WillZhang
 *
 */
public class JacksonUtils {
	private static final Logger log = Logger.getLogger(JacksonUtils.class);
	
	// can reuse, share globally
	private final static ObjectMapper objectMapper;
	
	static {
		//全局操作对象,可以通过这个对象设置全局解析json时的规则
		objectMapper = new ObjectMapper();
		
		// 为了使JSON视觉上的可读性，增加一行如下代码，注意，在生产中不需要这样，因为这样会增大Json的内容 
		if(!SystemHelper.isProduction()) {
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		}

		//当反序列化json时，未知属性会引起的反序列化被打断，这里我们禁用未知属性打断反序列化功能，  
        //因为，例如json里有10个属性，而我们的bean中只定义了2个属性，其它8个属性将被忽略  
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); 
		
		//配置mapper序例化json时忽略空属性  
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		
		//配置日期的默认展示方式
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	/**
	 * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
	 * 
	 * @param <T>
	 * @param jsonString
	 *            JSON字符串
	 * @param tr
	 *            TypeReference,例如: new TypeReference< List<FamousUser> >(){}
	 * @return List对象列表
	 * 
	 *         忽略掉一些没匹配上的字段
	 */
	@SuppressWarnings("unchecked")
	public static <T> T json2GenericObject(String jsonString,
			TypeReference<T> tr) {

		if (jsonString == null || "".equals(jsonString)) {
			return null;
		} else {
			try {
				return (T) objectMapper.readValue(jsonString, tr);
			} catch (Exception e) {
				log.warn("json error:" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Java对象转Json字符串
	 * 
	 * @param object
	 *            Java对象，可以是对象，数组，List,Map等
	 * @return json 字符串
	 */
	public static String toJson(Object object) {
		String jsonString = "";
		try {
			jsonString = objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			log.warn("json error:" + e.getMessage());
		}

		return jsonString;
	}

	/**
	 * Json字符串转Java对象
	 * 
	 * @param jsonString
	 * @param c
	 * @return
	 */
	public static Object json2Object(String jsonString, Class<?> c) {

		if (jsonString == null || "".equals(jsonString)) {
			return "";
		} else {
			try {
				return objectMapper.readValue(jsonString, c);
			} catch (Exception e) {
				log.warn("json error:" + e.getMessage());
			}

		}

		return "";
	}

	/**
	 * 根据json串和节点名返回节点
	 * 
	 * @param json
	 * @param nodeName
	 * @return
	 */
	public static JsonNode getNode(String json, String nodeName) {
		JsonNode node = null;
		try {
			node = getObjectMapper().readTree(json);
			return node.get(nodeName);
		} catch (JsonProcessingException e) {
			log.warn("json error:" + e.getMessage());
		} catch (IOException e) {
			log.warn("json error:" + e.getMessage());
		}
		return node;
	}

	/**
	 * JsonNode转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
	 * 
	 * @param <T>
	 * @param node
	 *            JsonParser
	 * @param tr
	 *            TypeReference,例如: new TypeReference< List<FamousUser> >(){}
	 * @return List对象列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonNode2GenericObject(JsonParser node,
			TypeReference<T> tr) {
		if (node == null || "".equals(node)) {
			return null;
		} else {
			try {
				return (T) objectMapper.readValue(node, tr);
			} catch (Exception e) {
				log.warn("json error:" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 根据json结果获取某个字段的值
	 */
	public static String getValueFromJsonStr(String jsonString, String paramName) {
		if (StringUtils.isNotBlank(jsonString)
				&& StringUtils.isNotBlank(paramName)) {
			try {
				JsonNode node = objectMapper.readTree(jsonString);
				if (node != null) {
					JsonNode value = node.get(paramName);
					if(value != null) {
						return value.asText();
					}
				}

			} catch (Exception e) {
				log.error("get param[" + paramName + "] value from json["
						+ jsonString + "] error:" + e);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		
		//使用测试
	}
	
	/**
	 * 对象序列化成json
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String bean2Json(Object obj) throws IOException {
		StringWriter sw = new StringWriter();
		JsonGenerator gen = new JsonFactory().createGenerator(sw);
		objectMapper.writeValue(gen, obj);
		gen.close();
		return sw.toString();
	}
	
	/**
	 * json反序列化成对象
	 * @param jsonStr
	 * @param objClass
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T json2Bean(String jsonStr, Class<T> objClass)
			throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(jsonStr, objClass);
	}
}
