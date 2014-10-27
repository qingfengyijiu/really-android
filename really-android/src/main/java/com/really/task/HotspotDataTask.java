package com.really.task;

import com.really.fragment.CommentHotspotFragment;
import com.really.service.ServerService;
import com.really.util.CommentResponseProcessor;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HotspotDataTask extends AsyncTask<Void, Void, String[]> {
	
	private static final String TAG = HotspotDataTask.class.getName();
	
	private CommentHotspotFragment fragment;
	
	public HotspotDataTask(CommentHotspotFragment hotspotFragment) {
		this.fragment = hotspotFragment;
	}
	
	@Override
	protected String[] doInBackground(Void... arg0) {
		ServerService serverService = this.fragment.getServerService();
		return serverService.getHotspots(fragment.getStartDate(), CommentHotspotFragment.QUERY_DAYS);
	}

	@Override
	protected void onPostExecute(String[] result) {
		if(result == null || result.length == 0) {
			Toast.makeText(this.fragment.getActivity(), "没有刷新到数据", Toast.LENGTH_SHORT).show();
		}
		CommentResponseProcessor.process(result, fragment.getListItems());
		Log.v(TAG, "刷新数据：" + result);
		//进行数据更新
		fragment.getAdapter().notifyDataSetChanged();
		//通知刷新结束
		fragment.getPullToRefreshListView().onRefreshComplete();
	}
	
}
