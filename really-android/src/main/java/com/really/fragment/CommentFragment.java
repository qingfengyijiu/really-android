package com.really.fragment;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.really.MainActivity;
import com.really.OpinionActivity2;
import com.really.R;
import com.really.adapter.CommentDataTaskAdapter;
import com.really.adapter.CommentListAdapter;
import com.really.pulltorefresh.library.PullToRefreshBase;
import com.really.pulltorefresh.library.PullToRefreshListView;
import com.really.pulltorefresh.library.PullToRefreshBase.Mode;
import com.really.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.really.service.ServerService;
import com.really.task.CommentDataTask;
import com.really.util.CommentResponseProcessor;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CommentFragment extends Fragment implements OnItemClickListener, OnRefreshListener2<ListView>  {

	private static final String TAG = "CommentFragment";
	
	private PullToRefreshListView pullToRefreshListView;
	
	private List<Map<String, Object>> listItems =  new LinkedList<Map<String, Object>>();
	
	private BaseAdapter adapter;
	
	private ListView actualListView;
	
	private int queryZone = DEFAULT_QUERY_ZONE;
	
	private int pageCount = DEFAULT_PAGE_COUNT;
	
	private static final int DEFAULT_QUERY_ZONE = 4;
	
	private static final int DEFAULT_PAGE_COUNT = 1;
	
	public ServerService getServerService() {
		if(this.getActivity() != null) {
			return ((MainActivity)this.getActivity()).getServerService();
		} else {
			return null;
		}
		
	}
	
	public CommentFragment() {
		super();
	}
	
	public CommentFragment(int queryZone) {
		this.queryZone = queryZone;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_comments, container, false);
		// 获取view中的子元素，并绑定事件
		this.pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		this.pullToRefreshListView.setMode(Mode.BOTH);
		this.pullToRefreshListView.setOnItemClickListener(this);
		this.pullToRefreshListView.setOnRefreshListener(this);
		this.actualListView = this.pullToRefreshListView.getRefreshableView();
		
		// Need to use the actual listview when registering for context menu
		this.registerForContextMenu(actualListView);
		
		//初始化UI数据
		this._initListView();
		this.adapter = new CommentListAdapter(this);
		this.actualListView.setAdapter(this.adapter);
		return view;
	}
	
	
	private void _initListView() {
		this.listItems = new LinkedList<Map<String, Object>>();
		String[] comments = this.getServerService().getComments(DEFAULT_QUERY_ZONE, DEFAULT_PAGE_COUNT);
		Log.v(TAG, "初始化列表：" + comments);
		CommentResponseProcessor.process(comments, this.listItems);
		this.addPageCount();
	}
	
	public void addPageCount() {
		this.pageCount++;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	

	public PullToRefreshListView getPullToRefreshListView() {
		return pullToRefreshListView;
	}

	public void setPullToRefreshListView(PullToRefreshListView pullToRefreshListView) {
		this.pullToRefreshListView = pullToRefreshListView;
	}

	public List<Map<String, Object>> getListItems() {
		return listItems;
	}

	public void setListItems(List<Map<String, Object>> listItems) {
		this.listItems = listItems;
	}

	public BaseAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		Context ctx = CommentFragment.this.getActivity().getApplicationContext();
		Toast.makeText(ctx, "下拉刷新", Toast.LENGTH_SHORT).show();
		String label = DateUtils.formatDateTime(ctx, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		new CommentDataTask(this, new CommentDataTaskAdapter(this.getActivity(), this.queryZone, this.pageCount)).execute();
		this.addPageCount();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		Context ctx = CommentFragment.this.getActivity().getApplicationContext();
		Toast.makeText(ctx, "上拉获取更多", Toast.LENGTH_SHORT).show();
		String label = DateUtils.formatDateTime(ctx, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		new CommentDataTask(this, new CommentDataTaskAdapter(this.getActivity(), this.queryZone, this.pageCount)).execute();
		this.addPageCount();
	}

	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View v, int paramInt, long paramLong) {
		Intent intent = new Intent();
		intent.setClass(CommentFragment.this.getActivity(), OpinionActivity2.class);
		intent.putExtra("comment_id", ((TextView) v.findViewById(R.id.textview_comment_id)).getText());
		this.getActivity().startActivity(intent);
	}

}
