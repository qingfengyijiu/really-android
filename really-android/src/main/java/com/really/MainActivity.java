package com.really;

import com.really.fragment.CommentFragment;
import com.really.fragment.CommentHotspotFragment;
import com.really.news.NewsFetchActivity;
import com.really.service.SecurityContextUtils;
import com.really.service.ServerService;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private ActionBar actionBar;
	
	private ServerService serverService;
	
	private TextView circleAction;
	
	private TextView discoverAction;
	
	private CommentFragment commentFragment;
	
	private CommentHotspotFragment hotspotFragment;
	
	private static final int TAB_COMMENT = 0;
	private static final int TAB_HOTSPOT = 1;
	
	private FragmentManager fragmentManager;
	
	public ServerService getServerService() {
		return serverService;
	}

	public void setServerService(ServerService serverService) {
		this.serverService = serverService;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.serverService = new ServerService(this);
		this._initView();
		
		fragmentManager = getFragmentManager(); 
		// 第一次启动时选中评论流tab
		this._setTabSelection(TAB_COMMENT);
		
	}
	
	private void _initView() {
		actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = inflator.inflate(R.layout.activity_main_head, null);
	    ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    actionBar.setCustomView(view, layout);
		actionBar.show();
		ImageButton editAction = (ImageButton)view.findViewById(R.id.action_edit);
		editAction.setOnClickListener(this);
		ImageButton settingsAction = (ImageButton)view.findViewById(R.id.action_settings);
		settingsAction.setOnClickListener(this);
		this.circleAction = (TextView)view.findViewById(R.id.action_circle);
		this.circleAction.setOnClickListener(this);
		this.discoverAction = (TextView)view.findViewById(R.id.action_discover);
		this.discoverAction.setOnClickListener(this);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.action_edit :
				if(!SecurityContextUtils.checkhasLoginAndAutoLogin(this)) {
					_toEditNews();
				}
				break;
			case R.id.action_discover :
				this._setTabSelection(TAB_HOTSPOT);
				break;
			case R.id.action_circle :
				this._setTabSelection(TAB_COMMENT);
				break;
			default:
				break;
		}
		
	}
	
	private void _toEditNews() {
		Intent intent = new Intent();
		intent.setClass(this, NewsFetchActivity.class);
		this.startActivity(intent);
	}
	
	private void _setTabSelection(int index) {
		// 每次选中之前，先清除上次的状态
		this._clearSelection();
		// 开启一个新的事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();  
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况  
        this._hideFragments(transaction);  
        switch(index) {
        case TAB_COMMENT :
        	//当点击了评论列表tab时，改变文字的颜色和背景
        	this.circleAction.setBackgroundResource(R.color.tab_selected);
        	this.circleAction.setTextColor(R.color.tab_text_selected);
        	transaction.replace(R.id.fragment_container, new CommentFragment());
        	transaction.addToBackStack(null);
//        	if(commentFragment == null) {
//        		commentFragment = new CommentFragment();
//        		transaction.add(R.id.fragment_container, commentFragment);
//        	} else {
//        		transaction.show(commentFragment);
//        		commentFragment.getAdapter().notifyDataSetChanged();
//        	}
        	break;
        case TAB_HOTSPOT :
        	//当点击了热点tab时，改变文字的颜色和背景
        	this.discoverAction.setBackgroundResource(R.color.tab_selected);
        	this.discoverAction.setTextColor(R.color.tab_text_selected);
        	transaction.replace(R.id.fragment_container, new CommentHotspotFragment());
        	transaction.addToBackStack(null);
//        	if(hotspotFragment == null) {
//        		hotspotFragment = new CommentHotspotFragment();
//        		transaction.add(R.id.fragment_container, hotspotFragment);
//        	} else {
//        		transaction.show(hotspotFragment);
//        		hotspotFragment.getAdapter().notifyDataSetChanged();
//        	}
        	break;
        default :
        	break;
        }
        // 最后提交事务
        transaction.commit();
	}
	
	private void _clearSelection() {
		this.circleAction.setBackgroundResource(R.color.tab_unselected);
		this.circleAction.setTextColor(R.color.tab_text_unselected);
		this.discoverAction.setBackgroundResource(R.color.tab_unselected);
		this.discoverAction.setTextColor(R.color.tab_text_unselected);
	}
	
	/**
	 * 将所有的fragment置为隐藏状态
	 * @param transaction	用于对Fragment执行操作的事务
	 */
	private void _hideFragments(FragmentTransaction transaction) {
		if(commentFragment != null) {
			transaction.hide(commentFragment);
		}
		if(hotspotFragment != null) {
			transaction.hide(hotspotFragment);
		}
	}
}
