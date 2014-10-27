package com.really;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class WelcomeActivity extends Activity {

	private AlphaAnimation start_anima;
	
	private View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.activity_welcome, null);
		setContentView(view);
		_initView();
		_initData();
	}
	
	private void _initData() {
		start_anima = new AlphaAnimation(0.3f, 1.0f);
		start_anima.setDuration(2000);
		view.startAnimation(start_anima);
		start_anima.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				redirectTo();
			}
		});
	}
	
	private void _initView() {
		ActionBar actionBar = this.getActionBar();
		if(null != actionBar) {
			actionBar.hide();
		}
	}

	private void redirectTo() {
		/*User lastLoginUser = SecurityContextUtils.getLastLoginUser(getApplicationContext());
		boolean isNeedLogin = true;
		if(null != lastLoginUser) {
			ServerService service = new ServerService(this);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("username", lastLoginUser.getUsername());
			data.put("password", lastLoginUser.getPassword());
			Map<String, Object> result = service.login(data);
			String status = (String) result.get("status");
			if("0".equals(status)) {
				isNeedLogin = false;
			}
		}
		if(isNeedLogin) {
			startActivity(new Intent(getApplicationContext(), LoginActivity.class));
		} else {
			SecurityContextUtils.setCurrentUser(getApplicationContext(), lastLoginUser);
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
		}*/
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}
	
}
