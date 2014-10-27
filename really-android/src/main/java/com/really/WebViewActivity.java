package com.really;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class WebViewActivity extends Activity implements OnClickListener {
	
	WebView webview;
	
	private void _initView() {
		ActionBar actionBar = this.getActionBar();
		actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = inflator.inflate(R.layout.activity_webview_header, null);
	    ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    actionBar.setCustomView(view, layout);
		actionBar.show();
		ImageButton view_back = (ImageButton)view.findViewById(R.id.imagebutton_webview_back);
		view_back.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_webview);
		_initView();
		Intent intent = this.getIntent();
		String url = intent.getStringExtra("news_url");
		this.webview = (WebView)this.findViewById(R.id.webview);
		WebSettings webSettings = this.webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		this.webview.setWebViewClient(new WebViewClient());
		Log.v("WebViewActivity", "开始打开网页:" + url);
		this.webview.loadUrl(url);
	}
	
	 /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webview.canGoBack())
        {
            // 返回键退回
            this.webview.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id) {
		case R.id.imagebutton_webview_back :
			if(this.webview.canGoBack()) {
				this.webview.goBack();
			} else {
				this.finish();
			}
			break;
		 default :
			break;
		}
		
	}

	
}
