package com.really.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ErrorMessageHelper {

	public static void showErrorMessage(Context context,String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		Log.v(context.getClass().getName(), message);
	}
	
	public static void showErrorMessage(Context context, Throwable e) {
		String message = e.getMessage();
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		Log.v(context.getClass().getName(), message);
	}
}
