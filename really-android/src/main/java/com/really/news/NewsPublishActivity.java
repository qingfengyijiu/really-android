package com.really.news;

import java.util.HashMap;
import java.util.Map;

import com.really.R;
import com.really.service.SecurityContextUtils;
import com.really.service.ServerService;
import com.really.util.BackgroundRandomGenerator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewsPublishActivity extends Activity implements OnClickListener {
	
	private TextView textview_news_title;
	
	private EditText edittext_news_content;
	
	private Button button_news_publish;
	
	private String newsUrl;
	
	private String newsTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_news_publish);
		this.textview_news_title = (TextView)this.findViewById(R.id.textview_news_title);
		this.edittext_news_content = (EditText)this.findViewById(R.id.edittext_news_content);
		this.button_news_publish = (Button)this.findViewById(R.id.button_news_publish);
		Intent intent = this.getIntent();
		this.newsUrl = intent.getStringExtra("news_url");
		this.newsTitle = intent.getStringExtra("news_title");
		this.textview_news_title.setText(newsTitle);
		this.button_news_publish.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.button_news_publish :
				long newsId = _newsPublish();
				_toComment(newsId);
				break;
			default :
				break;
		}
		
	}
	
	private long _newsPublish() {
		long userId = SecurityContextUtils.getCurrentUser().getId();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("userId", userId);
		data.put("news_url", this.newsUrl);
		data.put("news_title", this.newsTitle);
		data.put("news_content", this.edittext_news_content.getText().toString());
		data.put("backgroundColor", BackgroundRandomGenerator.generate());
		ServerService serverService = new ServerService(this);
		return serverService.publishNews(data);
	}
	
	private void _toComment(long newsId) {
		Intent intent = new Intent();
		intent.setClass(this, NewsCommentActivity.class);
		intent.putExtra("news_id", newsId);
		this.startActivity(intent);
	}

	
}
