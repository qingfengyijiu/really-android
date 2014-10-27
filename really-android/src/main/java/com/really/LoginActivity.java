package com.really;

import java.util.HashMap;
import java.util.Map;

import com.really.domain.User;
import com.really.exception.BusinessException;
import com.really.exception.SystemException;
import com.really.service.ErrorMessageHelper;
import com.really.service.SecurityContextUtils;
import com.really.service.ServerService;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "LoginActivity";
	
	private EditText edittext_username;
	
	private EditText edittext_password;
	
	private Button button_login;
	
	private TextView textview_register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		_initView();
		this.edittext_username = (EditText)this.findViewById(R.id.edittext_username);
		this.edittext_password = (EditText)this.findViewById(R.id.edittext_password);
		this.button_login = (Button)this.findViewById(R.id.button_login);
		this.textview_register = (TextView)this.findViewById(R.id.textview_register);
		
		this.button_login.setOnClickListener(this);
		this.textview_register.setOnClickListener(this);
	}
	
	private void _initView() {
		ActionBar actionBar = this.getActionBar();
		if(null != actionBar) {
			actionBar.hide();
		}
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.button_login :
			_login();
			break;
		case R.id.textview_register :
			_toRegister();
			break;
		default :
			break;
		}
		
	}
	
	private void _login() {
		String username = this.edittext_username.getText().toString();
		String password = this.edittext_password.getText().toString();
		if(username == null || username.trim().length() <= 0) {
			ErrorMessageHelper.showErrorMessage(this, "用户名不能为空");
			this.edittext_username.requestFocus();
			return;
		}
		if(password == null || password.trim().length() == 0) {
			ErrorMessageHelper.showErrorMessage(this, "密码不能为空");
			this.edittext_password.requestFocus();
			return;
		}
		ServerService service = new ServerService(this);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("username", username);
		data.put("password", password);
		Map<String, Object> result = null;
		try {
			result = service.login(data);
			Log.v(TAG, "登录返回结果：" + result);
		} catch (SystemException e) {
			ErrorMessageHelper.showErrorMessage(this, e);
			return;
		}
		String status = result.get("status").toString();
		if("0".equals(status)) {
			User user = new User();
			user.setId(Long.parseLong(result.get("userId").toString()));
			user.setUsername(username);
			user.setPassword(password);
			user.setEmail((String)result.get("email"));
			user.setLastLoginTime((Long)result.get("lastLoginTime"));
			SecurityContextUtils.setCurrentUser(getApplicationContext(), user);
			//登录成功，返回之前界面
			finish();
		} else {
			String message = BusinessException.getMessage((String)result.get("error_code"));
			ErrorMessageHelper.showErrorMessage(getApplicationContext(), message);
		}
	}
	
	private void _toRegister() {
		this.startActivity(new Intent(this, RegisterActivity.class));
	}
}
