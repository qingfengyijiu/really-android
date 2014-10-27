package com.really.news;

import java.util.HashMap;
import java.util.Map;

import com.really.MainActivity;
import com.really.R;
import com.really.service.SecurityContextUtils;
import com.really.service.ServerService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewsCommentActivity extends Activity implements OnClickListener {
	
	private EditText edittext_news_comment;
	
	private Button button_news_comment;
	
	private long newsId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_news_comment);
		this.edittext_news_comment = (EditText)this.findViewById(R.id.edittext_news_comment);
		this.button_news_comment = (Button)this.findViewById(R.id.button_news_comment);
		this.button_news_comment.setOnClickListener(this);
		Intent intent = this.getIntent();
		this.newsId = intent.getLongExtra("news_id", -1L);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()) {
			case R.id.button_news_comment :
				//NewsHelperService newsHelperService = ..;
				_newsComment();
				_toMain();
				break;
			default:
				break;
		}
		
	}
	
	private void _newsComment() {
		long userId = SecurityContextUtils.getCurrentUser().getId();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("news_id", this.newsId);
		data.put("userId", userId);
		data.put("comment_content", this.edittext_news_comment.getText().toString());
		ServerService serverService = new ServerService(this);
		serverService.commentNews(data);
	}
	
	private void _toMain() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		this.startActivity(intent);
	}
	
	

}
