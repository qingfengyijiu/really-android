package com.really.util;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static <T> T parse(String json, Class<T> T) {
		try {
			return (T)objectMapper.readValue(json, T);
		} catch (Exception e) {
			return null;
		} 
	}
	
	public static String generate(Map<String, Object> data) {
		try {
			return objectMapper.writeValueAsString(data);
		} catch (Exception e) {
			return null;
		}
	}
}
