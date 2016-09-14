package com.tianxun.framework.utils.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianxun.framework.utils.ExceptionLogUtil;

import net.sf.json.xml.XMLSerializer;

/** 
 * Json串处理工具类 
 * json,list,map等之间的各种转化  
 *  
 */  

public class JsonUtils {

    private static Logger logger = Logger.getLogger(JsonUtils.class);

    /** 
     * 将json格式的字符串转化为jsonobject 
     * @param str 
     * @return   
     */
    public static JSONObject stringToJsonObject(String str) {
        JSONObject jsonObject = null;
        if (StringUtils.isNotBlank(str)) {
            try {
                jsonObject = JSONObject.parseObject(str);
            } catch (Exception e) {
            	logger.error("stringToJsonObject", e);
            }
        }
        return jsonObject;
    }
    
    
    public static void main(String[] args) {
    	String reamrkString = "{\"osVersion\":\"9.3\",\"device\":{\"vendor\":\"Apple\",\"model\":\"iPhone\"},\"deviceId\":\"1451903411781.31304\",\"appVersion\":\"4.9.0\",\"network\":\"wifi\"}";
		
    	JSONObject json = JsonUtils.stringToJsonObject(reamrkString);
    	
    	if(json != null) {
    		System.out.println("==============:"+json.toJSONString());
    	}
	}
    
    /**
     * 
     * 把复杂结构Map(里边可嵌套List和Map)结果转换成JSON对象
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public static JSONObject fromMapWithList(Map<Object, Object> map) {  
        Iterator<Object> it = map.keySet().iterator();
        JSONObject jsonObject = new JSONObject();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object value = map.get(key);
            
            // 空值的话不要,减少输出内容的长度
            if(null == value) {
                continue;
            }
            
            if(value instanceof Map) { // 如果是Map类型,嵌套调用
                JSONObject jsonObjTmp = fromMapWithList((Map<Object, Object>)value);
                jsonObject.put(key, jsonObjTmp);
            } else if(value instanceof List) { // 如果是List类型
                List<?> liVal = (List<?>)value;
                if(liVal.isEmpty()) {
                    continue;
                }
                JSONArray jsonArrTmp = (JSONArray)JSONArray.toJSON(liVal); 
                jsonObject.put(key, jsonArrTmp);
            } else { // 简单类型
                if("".equals(value)){
                    continue;
                }
                jsonObject.put(key, value);   
            }                        
        }
        return jsonObject;
    }    
    
    /**
     * 
     * 把复杂结构List(里边可嵌套List和Map)结果转换成JSON对象
     * 
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public static JSONArray fromListWithMap(List<?> list) {  
        JSONArray options = new JSONArray();
        for (Object obj : list) {
            if(obj instanceof Map) {
                JSONObject jsonObjTmp = fromMapWithList((Map<Object, Object>)obj);
                options.add(jsonObjTmp);
            }
        }
        return options;
    }     
    
    /** 
     * 从指定的List<map>对象中获取要设置的值后组装成返回给前台的JSON对象字符串. 
     * 主要用于给前台分页grid组装数据 
     * @param list    list<map>从数据库中查询出来的数据 
     * @param count   总数 
     * @return 
     */
    public static String fromMap(List<?> list, int count) {
        //将list<map>转化为jsonarray  
        JSONArray options = formListArray(list);
        JSONObject result = new JSONObject();
        result.put("result", options);
        result.put("totalCount", count);
        return result.toString();
    }

    /** 
     * 将Map<String,String>转化为JSONObject格式的字符串 
     * @param map 
     * @return {"success":true,"result":{}} 
     */
    public static String fromMapToJson(Map<String, String> map) {
        Iterator<String> it = map.keySet().iterator();
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        while (it.hasNext()) {
            String key = (String) it.next();
            jsonObject.put(key.toLowerCase(), map.get(key));
        }
        json.put("success", true);
        json.put("result", jsonObject.toString());
        return json.toString();
    }

    /** 
     *  将list<map>转化为jsonobject格式的字符串 
     * @param list 
     * @return {"result":{...}} 
     */
    public static String fromListMap(List<?> list) {
        JSONArray options = new JSONArray();
        for (Object obj : list) {
            Iterator<?> it = ((Map<?, ?>) obj).keySet().iterator();
            Object value;
            JSONObject option = new JSONObject();
            while (it.hasNext()) {
                String key = (String) it.next();
                value = ((Map<?, ?>) obj).get(key);
                value = value != null ? value : "";
                option.put(key.toLowerCase(), value);
            }
            options.add(option);
        }
        JSONObject result = new JSONObject();
        result.put("result", options.toString());
        return result.toString();
    }

    /** 
     * 从list<map>转化为treenode的json 
     * @param list 
     * @return 
     */
    public static String toAsynTreeJson(List<?> list) {
        JSONArray ja = new JSONArray();
        for (Object obj : list) {
            Iterator<?> it = ((Map<?, ?>) obj).keySet().iterator();
            JSONObject option = new JSONObject();
            while (it.hasNext()) {
                String key = (String) it.next();
                Object value = ((Map<?, ?>) obj).get(key);
                key = key.toLowerCase();
                value = value == null ? "" : value.toString();
                //是否含有带checkbox的treenode  
                if (key.equals("checked")) {
                    boolean check = value.equals("1") ? true : false;
                    option.put("checked", check);
                }
                //是为叶子节点还是为非叶子节点  
                else if ("leaf".equals(key)) {
                    boolean leaf = value.toString().trim().equals("1") ? true
                            : false;
                    option.put("leaf", leaf);
                } else if ("draggable".equals(key)) {
                    boolean draggable = value.toString().trim().equals("1") ? true
                            : false;
                    option.put("draggable", draggable);
                } else
                    option.put(key, value);
            }
            ja.add(option);
        }
        return ja.toString();
    }

    /** 
     * 将list<map>转化为jsonarray 
     * 此JSONArray的格式将会是这样[{},{}] 
     * @param list  
     * @return 
     */
    public static JSONArray formListArray(List<?> list) {
        JSONArray options = new JSONArray();
        for (Object obj : list) {
            Iterator<?> it = ((Map<?, ?>) obj).keySet().iterator();
            JSONObject option = new JSONObject();
            while (it.hasNext()) {
                String key = (String) it.next();
                Object value = ((Map<?, ?>) obj).get(key);
                value = value != null ? value : "";
                option.put(key.toLowerCase(), value);
            }
            options.add(option);
        }
        return options;
    }

    /** 
     * 从List<Model>转化成jsonArray 
     * list中装的对象为具体的po 
     * @param list 
     * @return 
     */
    public static JSONArray formListModelArray(List<?> list) {
        JSONArray options = new JSONArray();
        for (Object obj : list) {
            options.add(obj);
        }
        return options;
    }

    /** 
     * 将JSONArray逆向转化为List  list中可以为任何元素， 
     * @param json  符合JSONArray格式的字符串 
     *              可以是很复杂的格式，如[{},[],{a:[],b:{},c:{cc:[]...}},'test'] 
     * @return 
     */
    public static List<Object> JSONArrayConvertList(String json) {
        ArrayList<Object> list = new ArrayList<Object>();
        JSONArray ja = JSONArray.parseArray(json);
        populateArray(ja, list);
        return list;
    }

    private static void populateArray(JSONArray jsonArray, List<Object> list) {
        //循环遍历jsonarray  
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.get(i).getClass().equals(JSONArray.class)) { //如果元素是JSONArray类型  
                ArrayList<Object> _list = new ArrayList<Object>();
                list.add(_list);
                //递归遍历，此为深度遍历，先把最内层的jsonobject给遍历了  
                populateArray(jsonArray.getJSONArray(i), _list);
            } else if (jsonArray.get(i).getClass().equals(JSONObject.class)) { //如果是JSONObject类型  
                HashMap<String, Object> _map = new HashMap<String, Object>();
                list.add(_map);
                //遍历JSONObject  
                populate(jsonArray.getJSONObject(i), _map);
            } else { //如果都不是的话就直接加入到list中  
                list.add(jsonArray.get(i));
            }
        }
    }

    private static Map<String, Object> populate(JSONObject jsonObject, Map<String, Object> map) {
        for (Iterator<?> iterator = jsonObject.entrySet().iterator(); iterator
                .hasNext();) {
            String entryStr = String.valueOf(iterator.next());
            String key = entryStr.substring(0, entryStr.indexOf("="));
            if (jsonObject.get(key).getClass().equals(JSONObject.class)) {
                HashMap<String, Object> _map = new HashMap<String, Object>();
                map.put(key, _map);
                populate(jsonObject.getJSONObject(key), _map);
            } else if (jsonObject.get(key).getClass().equals(JSONArray.class)) {
                ArrayList<Object> list = new ArrayList<Object>();
                map.put(key, list);
                populateArray(jsonObject.getJSONArray(key), list);
            } else {
                map.put(key, jsonObject.get(key));
            }
        }
        return map;
    }

    /** 
     * Map转换成JSONObject 
     * @param map 
     * @return 
     */
    public static JSONObject populateToJsonObject(Map<?, ?> map) {
        JSONObject temp = new JSONObject();
        Iterator<?> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<?, ?> entry = (Entry<?, ?>) iter.next();
            String key = entry.getKey().toString();
            String value = entry.getValue() == null ? "" : entry.getValue()
                    .toString();
            temp.put(key, value);
        }
        return temp;
    }

    /** 
     *将json转化为xml 
     **/
    public static String jsonToXml(JSON json, String rootName) {
    	net.sf.json.JSON jsonSF = net.sf.json.JSONObject.fromObject(json.toJSONString());
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.setTypeHintsEnabled(false);
        xmlSerializer.setRootName(rootName);        
        return xmlSerializer.write(jsonSF);
    }

    /** 
     *将xml转化为json 
     **/
    public static JSON xmlToJson(String xml) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        net.sf.json.JSON json = xmlSerializer.read(xml);
        return JSON.parseObject(json.toString());
    }

    /**
     * json字符串转为对象
     * 
     * @param jsonStr
     * @return
     */
	public static <T> T getObjectByJson(String jsonStr, Class<T> c){
        T ob = null;
        try {
            ob = (T) JSONObject.parseObject(jsonStr, c);
        } catch (Exception e) {
            ExceptionLogUtil.logExceptionInfo(logger, e);
        }
        return ob;
    }
    
    public static String getJsonByObject(Object object){
    	String str = "";
    	JSONObject json = (JSONObject)JSONObject.toJSON(object);//将java对象转换为json对象  
    	str = json.toString();//将json对象转换为字符串  
    	return str;
    }
    
	public static Map<String, JSONObject> jsonArrayToMap(JSONArray jsonArray, String key){
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		for(int i =0; i<jsonArray.size() ; i ++){
			JSONObject jo = jsonArray.getJSONObject(i);
			map.put(jo.getString(key), jo);
		}
		return map;
	}
}