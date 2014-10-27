package com.really;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.really.adapter.OpinionListAdapter2;
import com.really.exception.BusinessException;
import com.really.exception.SystemException;
import com.really.pulltorefresh.library.PullToRefreshBase;
import com.really.pulltorefresh.library.PullToRefreshListView;
import com.really.pulltorefresh.library.PullToRefreshBase.Mode;
import com.really.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.really.service.ErrorMessageHelper;
import com.really.service.SecurityContextUtils;
import com.really.service.ServerService;
import com.really.util.OpinionResponseProcessor;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class OpinionActivity2 extends Activity implements OnClickListener, OnRefreshListener2<ListView> {

	private static final String TAG = OpinionActivity2.class.getName();
	
	private int background;
	
	private int alpha;
	
	private String news_url;
	
	private String news_title;
	
	private String news_truth_degree;
	
	private String comment_content;
	
	private String comment_attention;
	
	private long comment_id;
	
	private ImageView imageview_close;
	
	private LinearLayout layout_opinion_submit_open;
	
	private int pageCount = 1;

	private PullToRefreshListView pullToRefreshListView;

	private List<Map<String, Object>> listItems = new LinkedList<Map<String, Object>>();

	private BaseAdapter adapter;

	private ListView actualListView;
	
	private ServerService serverService;
	
	public ServerService getServerService() {
		return serverService;
	}
	
	void addPageCount() {
		this.pageCount++;
	}
	
	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public String getNews_url() {
		return news_url;
	}

	public void setNews_url(String news_url) {
		this.news_url = news_url;
	}

	public String getNews_title() {
		return news_title;
	}

	public void setNews_title(String news_title) {
		this.news_title = news_title;
	}

	public String getNews_truth_degree() {
		return news_truth_degree;
	}

	public void setNews_truth_degree(String news_truth_degree) {
		this.news_truth_degree = news_truth_degree;
	}

	public String getComment_attention() {
		return comment_attention;
	}

	public void setComment_attention(String comment_attention) {
		this.comment_attention = comment_attention;
	}

	public long getComment_id() {
		return comment_id;
	}

	public void setComment_id(long comment_id) {
		this.comment_id = comment_id;
	}
	
	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_opinion2);
		this.serverService = new ServerService(this);
		this._initIntentParams();
		this._initActionBar();
		this._initView();
		this._initListView();
	}
	
	private void _initActionBar() {
		// 隐藏头部actionbar
		ActionBar actionBar = this.getActionBar();
		if (null != actionBar) {
			actionBar.hide();
		}
	}
	
	private void _initView() {
		// 获取页面元素
		this.imageview_close = (ImageView)this.findViewById(R.id.imageview_close);
		this.layout_opinion_submit_open = (LinearLayout)this.findViewById(R.id.layout_opinion_submit_open);
		
		// 为页面元素添加监听器
		this.imageview_close.setOnClickListener(this);
		this.layout_opinion_submit_open.setOnClickListener(this);
	}
	
	private void _initListView() {
		this.pullToRefreshListView = (PullToRefreshListView) this.findViewById(R.id.pull_refresh_list);
		this.pullToRefreshListView.setMode(Mode.BOTH);
		this.pullToRefreshListView.setOnRefreshListener(this);
		this.actualListView = this.pullToRefreshListView.getRefreshableView();
		// Need to use the actual listview when registering for context menu
		this.registerForContextMenu(actualListView);

		// 初始化UI数据
		this.listItems = new LinkedList<Map<String, Object>>();
		// 添加comment_area项
		Map<String, Object> commentZoneData = new HashMap<String, Object>();
		commentZoneData.put("isCommentItem", true);
		this.listItems.add(commentZoneData);
		// 添加opinion_area项
		ServerService serverService = new ServerService(this);
		String[] opinions = serverService.getOpinions(this.comment_id, this.pageCount);
		Log.v(TAG, "init opinions: " + opinions);
		OpinionResponseProcessor.process(opinions, this.listItems);
		this.addPageCount();
		this.adapter = new OpinionListAdapter2(this, this.listItems);
		this.actualListView.setAdapter(this.adapter);
		//this._resetListViewHeight();
	}
	
	private void _initIntentParams() {
		Intent intent = this.getIntent();
		this.comment_id = Long.parseLong(intent.getStringExtra("comment_id"));
		ServerService serverService = new ServerService(this);
		try {
			Map<String, Object> result = serverService.queryCommentById(this.comment_id);
			String status = result.get("status").toString();
			if("1".equals(status)) {
				String error_code = result.get("error_code").toString();
				ErrorMessageHelper.showErrorMessage(this, BusinessException.getMessage(error_code));
				return;
			}
			this.comment_content = (String)result.get("comment_content");
			this.comment_attention = result.get("comment_attention").toString();
			this.news_truth_degree = new DecimalFormat("###.00").format(Double.parseDouble(result.get("news_truth_degree").toString()));
			this.news_title = (String)result.get("news_title");
			this.news_url = (String) result.get("news_url");
			this.background = (Integer)result.get("background");
			this.alpha = (Integer)result.get("alpha");
		} catch (SystemException e) {
			ErrorMessageHelper.showErrorMessage(this, e);
			return;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		Context ctx = this.getApplicationContext();
		Toast.makeText(ctx, "上拉获取更多", Toast.LENGTH_SHORT).show();
		String label = DateUtils.formatDateTime(ctx, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		new AsyncTask<Void, Void, String[]>() {

			@Override
			protected String[] doInBackground(Void... arg0) {
				ServerService serverService = new ServerService(OpinionActivity2.this);
				String[] data = serverService.getOpinions(OpinionActivity2.this.comment_id, OpinionActivity2.this.pageCount);
				return data;
			}
			
			@Override
			protected void onPostExecute(String[] result) {
				Log.v(TAG, "refreshing result: " + result);
				if(result == null || result.length == 0) {
					Toast.makeText(OpinionActivity2.this, "没有刷新到数据", Toast.LENGTH_SHORT).show();
					OpinionActivity2.this.addPageCount();
				}
				OpinionResponseProcessor.process(result, OpinionActivity2.this.listItems);
				// 进行数据更新
				OpinionActivity2.this.adapter.notifyDataSetChanged();
				// 通知刷新借宿
				OpinionActivity2.this.pullToRefreshListView.onRefreshComplete();
			}
			
		}.execute();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		Context ctx = this.getApplicationContext();
		Toast.makeText(ctx, "上拉获取更多", Toast.LENGTH_SHORT).show();
		String label = DateUtils.formatDateTime(ctx, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		new AsyncTask<Void, Void, String[]>() {

			@Override
			protected String[] doInBackground(Void... arg0) {
				ServerService serverService = OpinionActivity2.this.getServerService();
				String[] data = serverService.getOpinions(OpinionActivity2.this.comment_id, OpinionActivity2.this.pageCount);
				return data;
			}
			
			@Override
			protected void onPostExecute(String[] result) {
				Log.v(TAG, "refreshing result: " + result);
				if(result == null || result.length == 0) {
					Toast.makeText(OpinionActivity2.this, "没有刷新到数据", Toast.LENGTH_SHORT).show();
					OpinionActivity2.this.addPageCount();
				}
				OpinionResponseProcessor.process(result, OpinionActivity2.this.listItems);
				// 进行数据更新
				OpinionActivity2.this.adapter.notifyDataSetChanged();
				// 通知刷新借宿
				OpinionActivity2.this.pullToRefreshListView.onRefreshComplete();
			}
			
		}.execute();
//		new OpinionDataTask(this, new OpinionDataTaskAdapter(this.getActivity(), this.commentId, this.pageCount)).execute();
//		this.addPageCount();
		
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id) {
			case R.id.imageview_close :
			case R.id.textview_comment_content :
				_toMain();
				break;
			case R.id.layout_opinion_submit_open :
				if(!SecurityContextUtils.checkhasLoginAndAutoLogin(this)) {
					_toOpinionSubmit();
				}
				break;
			case R.id.imageview_news_link :
			case R.id.textview_news_title :
				_toWebView();
				break;
			default :
				break;
		}
	}
	
	private void _toMain() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}
	
	private void _toWebView() {
		Intent intent = new Intent();
		intent.setClass(this, WebViewActivity.class);
		intent.putExtra("news_url", this.news_url);
		this.startActivity(intent);
	}
	
	private void _toOpinionSubmit() {
		Intent intent = new Intent();
		intent.setClass(this, OpinionSubmitActivity.class);
		intent.putExtra("comment_id", this.comment_id);
		this.startActivity(intent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		this.recreate();
	}
	
	

}
