package com.really.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class HttpClientHelper {

	public static String doGet(String url) {
		String responseStr = null;
		HttpParams httpParams = new BasicHttpParams();
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpGet getMethod = new HttpGet(url);
		getMethod.addHeader("Content-Type", "application/json;charset=utf-8");
		try {
			HttpResponse response = httpClient.execute(getMethod);
			responseStr = _processHttpResponse(response);
		} catch (Exception e) {
			return null;
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}
		return responseStr;
	}

	public static String doPost(String url, Map<String, Object> data) {
		String responseStr = null;
		String requestStr = JsonHelper.generate(data);
		HttpParams httpParams = new BasicHttpParams();
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpPost postMethod = new HttpPost(url);
		StringEntity requestEntity;
		try {
			requestEntity = new StringEntity(requestStr);
			requestEntity.setContentType("application/json;charset=utf-8");
			postMethod.setEntity(requestEntity);
			postMethod.addHeader("Content-Type", "application/json;charset=utf-8");
			HttpResponse response = httpClient.execute(postMethod);
			responseStr = _processHttpResponse(response);
		} catch (Exception e) {
			return null;
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}
		return responseStr;
	}

	private static String _processHttpResponse(HttpResponse response) {
		BufferedInputStream in = null;
		try {
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_NOT_IMPLEMENTED) {
				return null;
			}
			in = new BufferedInputStream(response.getEntity().getContent());
			byte[] bt = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while (in.read(bt) != -1) {
				sb.append(new String(bt, 0, bt.length, "UTF-8"));
			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if(in != null) {
					in.close();
				}
			} catch (IOException e) {
				in = null;
			}
		}
	}

}
