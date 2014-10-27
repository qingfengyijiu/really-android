package com.really.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.really.MainActivity;
import com.really.R;
import com.really.exception.SystemException;
import com.really.service.ErrorMessageHelper;
import com.really.service.SecurityContextUtils;
import com.really.service.ServerService;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewsCommentActivity2 extends Activity implements OnClickListener {
	
	private static final String TAG = "NewsCommentActivity2";
	
	private String news_url;
	
	private String news_title;
	
	private TextView textview_news_title;
	
	private EditText edittext_news_comment;
	
	private TextView textview_news_comment;
	
	private ImageView imageview_close;
	
	private LinearLayout layout_news_comment;
	
	private GestureDetector gestureDetector;
	
	private int bgIndex = 0;
	
	private int alpha = -1;
	
	private List<Integer> bgResourceIds = new ArrayList<Integer>();
	
	private List<Drawable> bgs = new ArrayList<Drawable>();
	
	public Drawable getCurrentBg() {
		if(this.layout_news_comment != null) {
			return this.layout_news_comment.getBackground();
		} else {
			return null;
		}
	}
	
	public int getCurrentBgResourceId() {
		return this.bgResourceIds.get(this.bgIndex);
	}
	
	private GestureDetector.OnGestureListener onGestureListener = 
			new GestureDetector.SimpleOnGestureListener() {
		@Override  
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  float velocityY) {  
            float x = e2.getX() - e1.getX();  
            float y = e2.getY() - e1.getY(); 
            float absX = Math.abs(x);
            float absY = Math.abs(y);
            
            if(absX - absY >= 0) {
            	if (x > 0) {  
                    // 向右滑动
                	Log.v(TAG, "flip to right");
                	NewsCommentActivity2.this.bgIndex++;
                	_changeBg();
                } else if (x < 0) {  
                    // 向左滑动
                	Log.v(TAG, "flip to left");
                	NewsCommentActivity2.this.bgIndex--;
                	_changeBg();
                }  
            } else {
            	if (y > 0) {
                	// 向下滑动
            		Log.v(TAG, "flip to bottom");
            		NewsCommentActivity2.this.alpha -= 20;
            		_changeBgAlpha();
                } else if (y < 0) {
                	// 向上滑动
                	Log.v(TAG, "flip to top");
                	NewsCommentActivity2.this.alpha += 20;
                	_changeBgAlpha();
                }
            }
            return true;
        }  
		
	};
	
	private void _changeBg() {
		int size = NewsCommentActivity2.this.bgs.size();
		if(size > 0 && NewsCommentActivity2.this.layout_news_comment != null) {
			int index = this.bgIndex % size;
			if(index < 0) {
				index += size;
			}
			NewsCommentActivity2.this.layout_news_comment.setBackground(NewsCommentActivity2.this.bgs.get(index));
		}
	}
	
	
	private void _changeBgAlpha() {
		if(this.getCurrentBg() != null) {
			if(this.alpha == -1) {
				// 初始透明度居中开始
				this.alpha = 127;
			}
			int al = this.alpha % 255;
			if(al < 0) {
				al += 255;
			}
			this.getCurrentBg().setAlpha(al);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_news_comment2);
		_initBgs();
		_initParamsFromIntent();
		_initView();
		this.gestureDetector = new GestureDetector(this, onGestureListener);
		
	}
	
	private void _initBgs() {
		Resources resources = this.getResources();
		Drawable news_comment_bg_01 = resources.getDrawable(R.drawable.news_comment_bg_01);
		this.bgs.add(news_comment_bg_01);
		this.bgResourceIds.add(R.drawable.news_comment_bg_01);
		Drawable news_comment_bg_02 = resources.getDrawable(R.drawable.news_comment_bg_02);
		this.bgs.add(news_comment_bg_02);
		this.bgResourceIds.add(R.drawable.news_comment_bg_02);
		Drawable news_comment_bg_03 = resources.getDrawable(R.drawable.news_comment_bg_03);
		this.bgs.add(news_comment_bg_03);
		this.bgResourceIds.add(R.drawable.news_comment_bg_03);
		Drawable news_comment_bg_04 = resources.getDrawable(R.drawable.news_comment_bg_04);
		this.bgs.add(news_comment_bg_04);
		this.bgResourceIds.add(R.drawable.news_comment_bg_04);
	}
	
	private void _initParamsFromIntent() {
		Intent intent = this.getIntent();
		this.news_url = intent.getStringExtra("news_url");
		this.news_title = intent.getStringExtra("news_title");
	}
	
	private void _initView() {
		ActionBar actionBar = this.getActionBar();
		if(null != actionBar) {
			actionBar.hide();
		}
		this.textview_news_title = (TextView)this.findViewById(R.id.textview_news_title);
		this.edittext_news_comment = (EditText)this.findViewById(R.id.edittext_news_comment);
		this.textview_news_comment = (TextView)this.findViewById(R.id.textview_news_comment);
		this.imageview_close = (ImageView)this.findViewById(R.id.imageview_close);
		this.layout_news_comment = (LinearLayout)this.findViewById(R.id.layout_news_comment);
		_initBg();
		Log.v(TAG,this.layout_news_comment.getBackground().getOpacity() + "-------------");
		
		this.textview_news_title.setText(this.news_title);
		this.edittext_news_comment.requestFocus();
		this.textview_news_comment.setOnClickListener(this);
		this.imageview_close.setOnClickListener(this);
	}
	
	private void _initBg() {
		if(this.bgs != null && this.bgs.size() > bgIndex && this.layout_news_comment != null) {
			this.layout_news_comment.setBackground(this.bgs.get(this.bgIndex));
		}
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.textview_news_comment :
				_commentNews();
				_toMain();
				break;
			case R.id.imageview_close :
				this.finish();
				break;
			default :
				break;
		}
		
	}
	
	private boolean _commentNews() {
		boolean result = true;
		long userId = SecurityContextUtils.getCurrentUser().getId();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("news_url", this.news_url);
		data.put("news_title", this.news_title);
		data.put("userId", userId);
		data.put("news_comment", this.edittext_news_comment.getText().toString());
		data.put("background", this.getCurrentBgResourceId());
		data.put("alpha", this.alpha);
		ServerService serverService = new ServerService(this);
		Log.v(TAG, "评论消息的数据：" + data);
		Map<String, Object> response = null;
		try {
			response = serverService.commentNews2(data);
		} catch (SystemException e) {
			ErrorMessageHelper.showErrorMessage(this, e);
			this.finish();
		}
		String status = (String)response.get("status");
		if("1".equals(status)) {
			Toast.makeText(this, "评论消息出错，请稍后重试", Toast.LENGTH_SHORT).show();
		}
		return result;
	}
	
	private void _toMain() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.gestureDetector.onTouchEvent(event);
		
	}
	
	
	
}
