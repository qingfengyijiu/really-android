package com.really.adapter;

import java.text.DecimalFormat;
import java.util.Map;

import com.really.R;
import com.really.fragment.CommentFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentListAdapter extends BaseAdapter {
	
	private static final String TAG = "CommentListAdapter";
	
    private CommentFragment fragment;
    
    private LayoutInflater listContainer;           //视图容器 
    
    public CommentListAdapter(CommentFragment fragment) {
		this.fragment = fragment;
		this.listContainer = LayoutInflater.from(fragment.getActivity());
	}
    
    public final class CommentListItemView {
    	
    	private LinearLayout layout_comment_container;
    	
    	private TextView textview_news_url;
    	
    	private TextView textview_news_title;
    	
    	private TextView textview_comment_id;
    	
    	private TextView textview_comment_content;
    	
    	private TextView textview_comment_attention;
    	
    	private TextView textview_news_truth_degree;
    	
    	private TextView textview_commment_container_background;
    }
	
	@Override
	public int getCount() {
		return this.fragment.getListItems().size();
	}

	@Override
	public Object getItem(int position) {
		return this.fragment.getListItems().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentListItemView listItemView = null;
		if(convertView == null) {
			listItemView = new CommentListItemView();
			convertView = this.listContainer.inflate(R.layout.comment_item, null);
			
			listItemView.layout_comment_container = (LinearLayout)convertView.findViewById(R.id.layout_comment_container);
			listItemView.textview_news_url = (TextView)convertView.findViewById(R.id.textview_news_url);
			listItemView.textview_news_title = (TextView)convertView.findViewById(R.id.textview_news_title);
			listItemView.textview_news_truth_degree = (TextView)convertView.findViewById(R.id.textview_news_truth_degree);
			listItemView.textview_comment_id = (TextView)convertView.findViewById(R.id.textview_comment_id);
			listItemView.textview_comment_content = (TextView)convertView.findViewById(R.id.textview_comment_content);
			listItemView.textview_comment_attention = (TextView)convertView.findViewById(R.id.textview_comment_attention);
			listItemView.textview_commment_container_background = (TextView)convertView.findViewById(R.id.textview_comment_container_background);
			convertView.setTag(listItemView);
		} else {
			listItemView = (CommentListItemView)convertView.getTag();
		}
		
		// 初始化UI
		Map<String, Object> itemData = this.fragment.getListItems().get(position);
		Log.v(TAG, "当前行数据：" + itemData);
		listItemView.textview_news_url.setText(itemData.get("news_url").toString());
		listItemView.textview_news_title.setText(itemData.get("news_title").toString());
		double news_truth_degree = Double.parseDouble(itemData.get("news_truth_degree").toString());
		DecimalFormat df = new DecimalFormat("###.00");
		String news_truth_degree_formated = df.format(news_truth_degree);
		listItemView.textview_news_truth_degree.setText(news_truth_degree_formated);
		listItemView.textview_comment_id.setText(itemData.get("comment_id").toString());
		listItemView.textview_comment_content.setText(itemData.get("comment_content").toString());
		listItemView.textview_comment_attention.setText(itemData.get("comment_attention").toString());
		int background = Integer.parseInt(itemData.get("background").toString());
		int alpha = Integer.parseInt(itemData.get("alpha").toString());
		listItemView.textview_commment_container_background.setText(background + "&" + alpha);;
		listItemView.layout_comment_container.setBackgroundResource(background);
		if(alpha != -1) {
			listItemView.layout_comment_container.setAlpha(alpha);
		}
		return convertView;
	}

}
