package com.really.service;

import java.util.HashMap;
import java.util.Map;

import com.really.LoginActivity;
import com.really.domain.User;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class SecurityContextUtils {
	
	private static final String TAG = SecurityContextUtils.class.getName();
	
	private static User currentUser;
	
	private static boolean login = false;
	
	public static boolean isLogin() {
		return login;
	}
	
	public static void setLogin(boolean isLogin) {
		login = isLogin;
	}

	
	public static void setCurrentUser(Context context,User user) {
		currentUser = user;
		DBOpenHelper dbHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		setLastLoginUser(db, user);
	}
	
	public static User getCurrentUser() {
		return currentUser;
	}
	
	public static void setLastLoginUser(SQLiteDatabase db, User user) {
		Long userId = user.getId();
		User lastLoginUser = getLastLoginUserByUserId(db, userId);
		ContentValues cv = new ContentValues();
		cv.put("USERNAME", user.getUsername());
		cv.put("PASSWORD", user.getPassword());
		cv.put("EMAIL", user.getEmail());
		cv.put("LAST_LOGIN_TIME", user.getLastLoginTime());
		if(null != lastLoginUser) {
			db.update("T_USER_LOGIN", cv, "PK = ?", new String[]{userId.toString()});
		} else {
			cv.put("PK", userId);
			db.insert("T_USER_LOGIN", null, cv);
		}
		setLogin(true);
	}
	
	public static User getLastLoginUserByUserId(SQLiteDatabase db, Long userId) {
		User user = null;
		String selection = null;
		String[] selectionArgs = null;
		if(null != userId) {
			selection = "PK = ?";
			selectionArgs = new String[] {userId.toString()};
		}
		Cursor cursor = db.query("T_USER_LOGIN", new String[]{"PK", "USERNAME", "PASSWORD", "EMAIL", "LAST_LOGIN_TIME"}, selection, selectionArgs, null, null, "LAST_LOGIN_TIME DESC", "0,1");
		if(cursor.moveToFirst()) {
			user = new User();
			userId = cursor.getLong(cursor.getColumnIndex("PK"));
			String username = cursor.getString(cursor.getColumnIndex("USERNAME"));
			String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
			String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
			user.setId(userId);
			user.setUsername(username);
			user.setPassword(password);
			user.setEmail(email);
		}
		return user;
	}
	
	public static User getLastLoginUser(Context context) {
		DBOpenHelper dbHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		return getLastLoginUserByUserId(db, null);
	}
	
	public static boolean checkhasLoginAndAutoLogin(Context context) {
		boolean isNeedLogin = true;
		if(isLogin()) {
			// 已经登录
			Log.v(TAG, "用户已经登录");
			isNeedLogin = false;
		} else {
			User lastLoginUser = SecurityContextUtils.getLastLoginUser(context);
			if(null != lastLoginUser) {
				ServerService service = new ServerService(context);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("username", lastLoginUser.getUsername());
				data.put("password", lastLoginUser.getPassword());
				Map<String, Object> result = service.login(data);
				String status = (String) result.get("status");
				if("0".equals(status)) {
					long lastLoginTime = (Long)result.get("lastLoginTime");
					lastLoginUser.setLastLoginTime(lastLoginTime);
					SecurityContextUtils.setCurrentUser(context, lastLoginUser);
					Log.v(TAG, "自动登录成功");
					setLogin(true);
					// 自动登录成功
					isNeedLogin = false;
				}
			}
		}
		if(isNeedLogin) {
			//需要登录，转向登录界面
			Toast.makeText(context, "您还没有登录呢", 300).show();
			context.startActivity(new Intent(context, LoginActivity.class));
		}
		return isNeedLogin;
	}
	
	
}
