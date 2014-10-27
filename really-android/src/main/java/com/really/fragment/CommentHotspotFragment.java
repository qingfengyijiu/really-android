package com.really.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.really.MainActivity;
import com.really.OpinionActivity2;
import com.really.R;
import com.really.adapter.HotspotListAdapter;
import com.really.pulltorefresh.library.PullToRefreshBase;
import com.really.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.really.pulltorefresh.library.PullToRefreshListView;
import com.really.pulltorefresh.library.PullToRefreshBase.Mode;
import com.really.service.ServerService;
import com.really.task.HotspotDataTask;
import com.really.util.HotspotResponseProcessor;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentHotspotFragment extends Fragment implements OnItemClickListener, OnRefreshListener2<ListView> {

	private static final String TAG = CommentHotspotFragment.class.getName();
	
	private PullToRefreshListView pullToRefreshListView;
	
	private List<Map<String, Object>> listItems =  new LinkedList<Map<String, Object>>();
	
	private BaseAdapter adapter;
	
	private ListView actualListView;
	
	private long startDate;
	
	public static final int QUERY_DAYS = 5;
	
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

	public ListView getActualListView() {
		return actualListView;
	}

	public void setActualListView(ListView actualListView) {
		this.actualListView = actualListView;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public void nextDate() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date(this.startDate));
		cal.add(Calendar.DAY_OF_MONTH, -QUERY_DAYS);
		this.startDate = cal.getTimeInMillis();
	}
	
	public ServerService getServerService() {
		return ((MainActivity)this.getActivity()).getServerService();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_comments, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 获取view中的子元素，并绑定事件
		this.pullToRefreshListView = (PullToRefreshListView) this.getActivity().findViewById(R.id.pull_refresh_list);
		this.pullToRefreshListView.setMode(Mode.BOTH);
		this.pullToRefreshListView.setOnItemClickListener(this);
		this.pullToRefreshListView.setOnRefreshListener(this);
		this.actualListView = this.pullToRefreshListView.getRefreshableView();
		// Need to use the actual listview when registering for context menu
		this.registerForContextMenu(actualListView);
		
		//初始化UI数据
		this._initListView();
		this.adapter = new HotspotListAdapter(this);
		this.actualListView.setAdapter(this.adapter);
	}
	
	private void _initListView() {
		this.listItems = new LinkedList<Map<String, Object>>();
		ServerService serverService = this.getServerService();
		this.startDate = new Date().getTime();
		String[] hotspots = serverService.getHotspots(this.startDate, QUERY_DAYS);
		Log.v(TAG, "初始化列表：" + hotspots);
		HotspotResponseProcessor.process(hotspots, this.listItems);
		this.nextDate();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		Context ctx = this.getActivity().getApplicationContext();
		Toast.makeText(ctx, "下拉刷新", Toast.LENGTH_SHORT).show();
		String label = DateUtils.formatDateTime(ctx, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		new HotspotDataTask(this).execute();
		this.nextDate();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		Context ctx = this.getActivity().getApplicationContext();
		Toast.makeText(ctx, "上拉获取更多", Toast.LENGTH_SHORT).show();
		String label = DateUtils.formatDateTime(ctx, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		new HotspotDataTask(this).execute();
		this.nextDate();
	}

	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View v, int paramInt, long paramLong) {
		Intent intent = new Intent();
		intent.setClass(this.getActivity(), OpinionActivity2.class);
		intent.putExtra("comment_id", ((TextView) v.findViewById(R.id.textview_comment_id)).getText());
		this.getActivity().startActivity(intent);
	}
	
	
	
}
