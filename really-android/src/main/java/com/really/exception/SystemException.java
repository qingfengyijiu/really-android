package com.really.exception;

import java.util.HashMap;

public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 1223505880393809270L;

	private String code;
	
	private static HashMap<String, String> messages = null;
	
	static {
		messages = new HashMap<String, String>();
		messages.put("001", "网络连接异常，请检查设备网络");
		messages.put("998", "服务器异常");
		messages.put("999", "参数不正确");
		messages.put("002", "密码不能为空");
		messages.put("003", "邮箱格式不正确");
		messages.put("004", "用户名已存在");
		messages.put("005", "用户名不存在");
		messages.put("006", "密码错误");
	}
	
	public SystemException(String code) {
		super();
		this.code = code;
	}
	
	public SystemException() {
		super();
	}
	
	public String getMessage() {
		return messages.get(this.code);
	}
	
	public static String getMessage(String code) {
		return messages.get(code);
	}
	
	public String getCode() {
		return code;
	}
	
}
