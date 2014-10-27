package com.really.util;

import java.util.List;
import java.util.Map;

public class HotspotResponseProcessor {
	
	private static final String TAG = HotspotResponseProcessor.class.getName();
	
	private HotspotResponseProcessor() {
		
	}

	
	@SuppressWarnings("unchecked")
	public static void process(String[] result, List<Map<String, Object>> listItems) {
		if(null == result || result.length == 0) {
			return;
		}
		for (String commentStr : result) {
			Map<String, Object> data = JsonHelper.parse(commentStr, Map.class);
			listItems.add(data);
		}
	}
}
