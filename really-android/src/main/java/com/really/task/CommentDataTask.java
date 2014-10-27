package com.really.task;

import com.really.adapter.DataTaskAdapter;
import com.really.fragment.CommentFragment;
import com.really.util.CommentResponseProcessor;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class CommentDataTask extends AsyncTask<Void, Void, String[]> {
	
	private static final String TAG = "CommentDataTask";
	
	private CommentFragment fragment;
	
	private DataTaskAdapter taskAdapter;
	
	public CommentDataTask(CommentFragment commentFragment, DataTaskAdapter taskAdapter) {
		this.fragment = commentFragment;
		this.taskAdapter = taskAdapter;
	}
	
	@Override
	protected String[] doInBackground(Void... arg0) {
		return taskAdapter.getData();
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
