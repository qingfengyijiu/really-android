package com.really.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.really.R;
import com.really.exception.SystemException;
import com.really.util.HttpClientHelper2;
import com.really.util.JsonHelper;

public class ServerService {
	
	private static final String TAG = "ServerService";
	
	private String serverBase;
	
	private Context context;
	
	public ServerService(Context context) {
		this.context = context;
		this.serverBase = context.getResources().getString(R.string.serverBase);
	}
	
	@SuppressWarnings("rawtypes")
	public String getLinkTitle(Map<String, Object> data) {
		String url = this.serverBase + "/news/parselink";
		String responseStr = HttpClientHelper2.doPost(url, data);
		Map responseData = JsonHelper.parse(responseStr, Map.class);
		if(responseData == null) {
			return null;
		}
		Object news_title = responseData.get("news_title");
		if(null != news_title) {
			return (String)news_title;
		} else {
			return null;
		}
	}
	
	public long publishNews(Map<String, Object> data) {
		String url = this.serverBase + "/news/add";
		String responseStr = HttpClientHelper2.doPost(url, data);
		return JsonHelper.parse(responseStr, Long.class);
	}
	
	public long commentNews(Map<String, Object> data) {
		String url = this.serverBase + "/comment/add";
		String responseStr = HttpClientHelper2.doPost(url, data);
		return JsonHelper.parse(responseStr, Long.class);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> commentNews2(Map<String, Object> data) {
		String url = this.serverBase + "/comment/add/news";
		String responseStr = HttpClientHelper2.doPost(url, data);
		return JsonHelper.parse(responseStr, Map.class);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryCommentById(long commentId) {
		String url = this.serverBase + "/comment/" + commentId;
		String responseStr = HttpClientHelper2.doGet(url);
		return JsonHelper.parse(responseStr, Map.class);
	}
	
	@SuppressWarnings("unchecked")
	public String[] getComments(int queryZone, int pageCount) {
		String[] results = null;
		String url = this.serverBase + "/comment/list/" + queryZone + "/" + pageCount;
		List<Map<String, Object>> comments = null;
		try {
			String responseStr = HttpClientHelper2.doGet(url);
			Log.v(TAG, "返回comments JSON数据：" + responseStr);
			comments = JsonHelper.parse(responseStr, List.class);
			Log.v(TAG, "返回comments JAVA数据：" + comments);
		} catch (SystemException e) {
			ErrorMessageHelper.showErrorMessage(this.context, e);
		}
		if(null == comments || comments.size() == 0) {
			return null;
		} else {
			results = new String[comments.size()];
			for(int i = 0; i < comments.size(); i++) {
				Map<String, Object> comment = comments.get(i);
				String commentStr = JsonHelper.generate(comment);
				results[i] = commentStr;
			}
		}
		return results;
	}
	
	public Map<String, Object> submitOpinion(Map<String, Object> data) {
		String url = this.serverBase + "/opinion/add";
		return this._postData(url, data);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> _postData(String url, Map<String, Object> data) {
		String responseStr = HttpClientHelper2.doPost(url, data);
		return JsonHelper.parse(responseStr, Map.class);
	}
	
	@SuppressWarnings("unchecked")
	public String[] getOpinions(long commentId, int pageCount) {
		String[] results = null;
		String url = this.serverBase + "/opinion/list/" + commentId + "/" + pageCount;
		List<Map<String, Object>> dataList = null;
		try {
			String responseStr = HttpClientHelper2.doGet(url);
			Log.v(TAG, "返回opinions JSON数据：" + responseStr);
			dataList = JsonHelper.parse(responseStr, List.class);
			Log.v(TAG, "返回opinions JAVA数据-----size：" + dataList.size());
			Log.v(TAG, "返回opinions JAVA数据：" + dataList);
		} catch (SystemException e) {
			ErrorMessageHelper.showErrorMessage(this.context, e);
		}
		if(null == dataList || dataList.size() == 0) {
			return null;
		} else {
			results = new String[dataList.size()];
			for(int i = 0; i < dataList.size(); i++) {
				Map<String, Object> opinion = dataList.get(i);
				results[i] = JsonHelper.generate(opinion);
			}
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public String[] getHotspots(long startDate, int days) {
		String[] results = null;
		String url = this.serverBase + "/comment/hotspot/query/" + startDate + "/" + days;
		Map<String, Object> responseResult = null;
		try {
			String responseStr = HttpClientHelper2.doGet(url);
			Log.v(TAG, "返回result JSON数据：" + responseStr);
			responseResult = JsonHelper.parse(responseStr, Map.class);
			Log.v(TAG, "返回result JAVA数据-----：" + responseResult);
			String status = responseResult.get("status").toString();
			if("1".equals(status)) {
				throw new SystemException("998");
			}
			List<List<Map<String, Object>>> hotspotList = (List<List<Map<String, Object>>>)responseResult.get("hotspots");
			List<String> resultList = new ArrayList<String>();
			if(hotspotList == null || hotspotList.size() == 0) {
				return null;
			}
			for(int i = 0; i < hotspotList.size(); i++) {
				List<Map<String, Object>> currentDayHotspotList = hotspotList.get(i);
				for(int j = 0; j < currentDayHotspotList.size(); j++) {
					Map<String, Object> currentHotspot = currentDayHotspotList.get(j);
					if(j == 0) {
						Date rankDay = new Date((Long)currentHotspot.get("rankDay"));
						String rankDayStr = new SimpleDateFormat("yyyy-MM-dd").format(rankDay);
						Map<String, Object> rankDayMap = new HashMap<String, Object>();
						rankDayMap.put("rankDay", rankDayStr);
						resultList.add(JsonHelper.generate(rankDayMap));
					}
					Map<String, Object> comment = (Map<String, Object>)currentHotspot.get("comment");
					Map<String, Object> news = (Map<String, Object>)comment.get("news");
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("news_url", news.get("url"));
					itemMap.put("news_title", news.get("title"));
					itemMap.put("news_truth_degree", news.get("truthDegree"));
					itemMap.put("comment_id", comment.get("id"));
					itemMap.put("comment_content", comment.get("content"));
					itemMap.put("comment_attention", comment.get("attention"));
					itemMap.put("background", comment.get("background"));
					itemMap.put("alpha", comment.get("alpha"));
					resultList.add(JsonHelper.generate(itemMap));
				}
			}
			results = resultList.toArray(new String[resultList.size()]);
		} catch (SystemException e) {
			throw e;
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> login(Map<String, Object> data) {
		String url = this.serverBase + "/user/login";
		String responseStr = HttpClientHelper2.doPost(url, data);
		Log.v(TAG, "登录返回json：" + responseStr);
		return JsonHelper.parse(responseStr, Map.class);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> register(Map<String, Object> data) {
		String url = this.serverBase + "/user/register";
		String responseStr = HttpClientHelper2.doPost(url, data);
		return JsonHelper.parse(responseStr, Map.class);
	}
	
	
}
