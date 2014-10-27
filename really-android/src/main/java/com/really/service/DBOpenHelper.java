package com.really.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	
	private static final String DB_NAME = "really_db";
	
	private SQLiteDatabase db = null;

	public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		this.db = database;
		db.execSQL("create table T_USER_LOGIN(PK integer, USERNAME nvarchar(20),PASSWORD nvarchar(20), EMAIL nvarchar(50), LAST_LOGIN_TIME integer, PRIMARY KEY(PK))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
