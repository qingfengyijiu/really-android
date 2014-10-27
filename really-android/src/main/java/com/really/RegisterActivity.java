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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity implements OnClickListener {

	private EditText edittext_username;
	
	private EditText edittext_password;
	
	private EditText edittext_password_repeat;
	
	private EditText edittext_email;
	
	private Button button_register;
	
	private TextView textview_login;
	
	private void _initView() {
		ActionBar actionBar = this.getActionBar();
		if(null != actionBar) {
			actionBar.hide();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_register);
		_initView();
		this.edittext_username = (EditText)this.findViewById(R.id.edittext_username);
		this.edittext_password = (EditText)this.findViewById(R.id.edittext_password);
		this.edittext_password_repeat = (EditText)this.findViewById(R.id.edittext_password_repeat);
		this.edittext_email = (EditText)this.findViewById(R.id.edittext_email);
		this.button_register = (Button)this.findViewById(R.id.button_register);
		this.textview_login = (TextView)this.findViewById(R.id.textview_login);
		
		this.button_register.setOnClickListener(this);
		this.textview_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.button_register :
				_register();
				break;
			case R.id.textview_login :
				_toLogin();
				break;
			default :
				break;
		}
	}
	
	private void _register() {
		String username = this.edittext_username.getText().toString();
		String password = this.edittext_password.getText().toString();
		String passwordRepeat = this.edittext_password_repeat.getText().toString();
		String email = this.edittext_email.getText().toString();
		if(null == username || username.trim().length() == 0) {
			ErrorMessageHelper.showErrorMessage(getApplicationContext(), "用户名不能为空");
			this.edittext_username.requestFocus();
			return;
		}
		if(null == password || password.trim().length() == 0) {
			ErrorMessageHelper.showErrorMessage(getApplicationContext(), "密码不能为空");
			this.edittext_password.requestFocus();
			return;
		}
		if(!password.equals(passwordRepeat)) {
			ErrorMessageHelper.showErrorMessage(getApplicationContext(), "两次输入的密码不一致");
			this.edittext_password_repeat.requestFocus();
			return;
		}
		if(null == email || email.trim().length() == 0) {
			ErrorMessageHelper.showErrorMessage(getApplicationContext(), "邮箱不能为空");
			this.edittext_email.requestFocus();
			return;
		}
		ServerService service = new ServerService(this);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("username", username);
		data.put("password", password);
		data.put("email", email);
		Map<String, Object> result = null;
		try {
			result = service.register(data);
		} catch (SystemException e) {
			ErrorMessageHelper.showErrorMessage(this, e.getMessage());
			return;
			
		}
		String status = result.get("status").toString();
		if("0".equals(status)) {
			long userId = Long.parseLong(result.get("userId").toString());
			long lastLoginTime = (Long)result.get("lastLoginTime");
			User user = new User();
			user.setId(userId);
			user.setUsername(username);
			user.setPassword(password);
			user.setEmail(email);
			user.setLastLoginTime(lastLoginTime);
			SecurityContextUtils.setCurrentUser(getApplicationContext(), user);
			//注册成功，返回之前界面
			this.finish();
		} else {
			String errorCode = (String)result.get("error_code");
			String message = BusinessException.getMessage(errorCode);
			ErrorMessageHelper.showErrorMessage(this, message);
		}
	}
	
	private void _toLogin() {
		this.startActivity(new Intent(this, LoginActivity.class));
	}
}
