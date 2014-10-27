package com.really.news;

import java.util.HashMap;
import java.util.Map;

import com.really.R;
import com.really.exception.SystemException;
import com.really.service.ErrorMessageHelper;
import com.really.service.ServerService;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsFetchActivity extends Activity implements OnClickListener {
	
	private static String TAG = "NewsFetchActivity";

	private EditText edittext_news_url;
	
	private TextView textview_news_fetch;
	
	private ImageView imageview_close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_news_fetch);
		this._initView();
	}
	
	private void _initView() {
		ActionBar actionBar = this.getActionBar();
		if(null != actionBar) {
			actionBar.hide();
		}
		this.edittext_news_url = (EditText)this.findViewById(R.id.edittext_news_url);
		this.textview_news_fetch = (TextView)this.findViewById(R.id.textview_news_fetch);
		this.imageview_close = (ImageView)this.findViewById(R.id.imageview_close);
		
		this.edittext_news_url.requestFocus();
		
		this.textview_news_fetch.setOnClickListener(this);
		this.imageview_close.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()) {
			case R.id.textview_news_fetch :
				Map<String, Object> news = _newsFetch();
				if(null == news) {
					ErrorMessageHelper.showErrorMessage(this, "获取新闻标题失败");
					return;
				}
				_toComment(news);
				break;
			case R.id.imageview_close :
				this.finish();
				break;
			default:
				break;
		}
	}
	
	private Map<String, Object> _newsFetch() {
		String newsUrl = this.edittext_news_url.getText().toString();
		newsUrl = this._processUrl(newsUrl);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("news_url", newsUrl);
		ServerService serverService = new ServerService(this);
		String newsTitle = null;
		try {
			newsTitle = serverService.getLinkTitle(data);
			Log.v(TAG, "获取到的新闻标题："+ newsTitle);
		} catch (SystemException e) {
			ErrorMessageHelper.showErrorMessage(this, e);
			this.finish();
		}
		
		if(null == newsTitle) {
			return null;
		}
		Map<String, Object> news = new HashMap<String, Object>();
		news.put("news_url", newsUrl);
		news.put("news_title", newsTitle);
		
		return news;
	}
	
	private String _processUrl(String url) {
		if(!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}
		return url;
	}
	
	/*private void _toPublish(Map<String, Object> newsData) {
		String newsUrl = ((String) newsData.get("news_url")).trim();
		String newsTitle = (String) newsData.get("news_title");
		Intent intent = new Intent();
		intent.setClass(NewsFetchActivity.this, NewsPublishActivity.class);
		intent.putExtra("news_url", newsUrl);
		intent.putExtra("news_title", newsTitle);
		NewsFetchActivity.this.startActivity(intent);
	}*/
	
	private void _toComment(Map<String, Object> newsData) {
		String newsUrl = ((String) newsData.get("news_url")).trim();
		String newsTitle = (String) newsData.get("news_title");
		Intent intent = new Intent();
		intent.setClass(this, NewsCommentActivity2.class);
		intent.putExtra("news_url", newsUrl);
		intent.putExtra("news_title", newsTitle);
		this.startActivity(intent);
	}
	
}
