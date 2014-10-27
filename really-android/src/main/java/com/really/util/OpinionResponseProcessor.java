package com.really.util;

import java.util.List;
import java.util.Map;

import android.util.Log;

public class OpinionResponseProcessor {
	
	private static final String TAG = "OpinionResponseProcessor";
	
	private OpinionResponseProcessor() {
		
	}

	
	@SuppressWarnings("unchecked")
	public static void process(String[] result, List<Map<String, Object>> listItems) {
		if(null == result || result.length == 0) {
			return;
		}
		for (String commentStr : result) {
			Log.v(TAG, "向适配器list中添加的json数据:"  + commentStr);
			Map<String, Object> data = JsonHelper.parse(commentStr, Map.class);
			Log.v(TAG, "向适配器list中添加的java数据:"  + data);
			data.put("opinion_id", data.get("id"));
			data.put("opinion_createTime", data.get("createTime"));
			data.put("opinion_content", data.get("content"));
			data.put("truthDegree", data.get("truthDegree"));
			listItems.add(data);
		}
	}
}
