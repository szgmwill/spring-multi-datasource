package com.tianxun.framework.common;

import java.io.IOException;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * @author charlesguo
 *
 */
public class TianxunGsonBuilder {
	
	private boolean serializeNulls;
	
	private boolean escapeHtmlChars;
	
	private String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	private boolean excludesFieldsWithoutExpose;
	
	public Gson create(){
		
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(String.class, new TypeAdapter<String>() {  
  
            @Override  
            public void write(JsonWriter out, String value) throws IOException {  
                if (value == null) {  
                    // out.nullValue();  
                    out.value(""); // 序列化时将 null 转为 ""  
                } else {  
                    out.value(value);  
                }  
            }  
  
            @Override  
            public String read(JsonReader in) throws IOException {  
                if (in.peek() == JsonToken.NULL) {  
                    in.nextNull();  
                    return null;  
                }  
                // return in.nextString();  
                String str = in.nextString();  
                if (str.equals("")) { // 反序列化时将 "" 转为 null  
                    return null;  
                } else {  
                    return str;  
                }  
            }  
  
        });  
		
		if (serializeNulls)
			builder.serializeNulls();
		if (!escapeHtmlChars)
			builder.disableHtmlEscaping();
		if (!Strings.isNullOrEmpty(dateFormat))
			builder.setDateFormat(dateFormat);
		if (excludesFieldsWithoutExpose) {
			builder.excludeFieldsWithoutExposeAnnotation();
		}
			
		return builder.create();
	}

	public boolean getSerializeNulls() {
		return serializeNulls;
	}

	public void setSerializeNulls(boolean serializeNulls) {
		this.serializeNulls = serializeNulls;
	}

	public boolean getEscapeHtmlChars() {
		return escapeHtmlChars;
	}

	public void setEscapeHtmlChars(boolean escapeHtmlChars) {
		this.escapeHtmlChars = escapeHtmlChars;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean getExcludesFieldsWithoutExpose() {
		return excludesFieldsWithoutExpose;
	}

	public void setExcludesFieldsWithoutExpose(boolean excludesFieldsWithoutExpose) {
		this.excludesFieldsWithoutExpose = excludesFieldsWithoutExpose;
	}

	
}
