package com.really.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.really.exception.SystemException;

public class HttpClientHelper2 {

	public static String doGet(String url) {
		BufferedInputStream in = null;
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		try {
			URL realUrl = new URL(url);
			connection = (HttpURLConnection)realUrl.openConnection();
			connection.connect();
			//in = new BufferedInputStream(connection.getInputStream());
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			/*byte[] bt = new byte[1024];
			StringBuffer sb = new StringBuffer();
			if(in.read(bt) != -1) {
				sb.append(new String(bt, 0, bt.length, "UTF-8"));
			}*/
			return sb.toString();
		} catch (Exception e) {
			throw new SystemException("001");
		} finally {
			if(null != in) {
				try {
					in.close();
				} catch (Exception e2) {
					in = null;				
				}
			}
			connection.disconnect();
		}
	}
	
	public static String doPost(String url, Map<String, Object> data) {
		BufferedInputStream in = null;
		OutputStream out = null;
		HttpURLConnection connection = null;
		String requestStr = JsonHelper.generate(data);
		try {
			URL realUrl = new URL(url);
			connection = (HttpURLConnection)realUrl.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			connection.connect();
			out = connection.getOutputStream();
			out.write(requestStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			in = new BufferedInputStream(connection.getInputStream());
			byte[] bt = new byte[1024];
			StringBuffer sb = new StringBuffer();
			if(in.read(bt) != -1) {
				sb.append(new String(bt, 0, bt.length, "UTF-8"));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new SystemException("001");
		} finally {
			if(null != in) {
				try {
					in.close();
				} catch (Exception e2) {
					in = null;				
				}
			}
			connection.disconnect();
		}
	}
}
