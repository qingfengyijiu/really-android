package com.really.adapter;

import java.text.DecimalFormat;
import java.util.Map;

import com.really.R;
import com.really.fragment.CommentHotspotFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HotspotListAdapter extends BaseAdapter {
	
	private static final String TAG = HotspotListAdapter.class.getName();
	
    private LayoutInflater listContainer;           //视图容器 
    
    private static final int VIEW_TYPE_COUNT = 2;
    
    private static final int VIEW_TYPE_DATE_HEADER = 0;
    
    private static final int VIEW_TYPE_HOTSPOT = 1;
    
    private CommentHotspotFragment fragment;
    
    public HotspotListAdapter(CommentHotspotFragment fragment) {
		this.fragment = fragment;
		this.listContainer = LayoutInflater.from(fragment.getActivity());
	}
    
    final class DateHeaderViewHolder {

		private TextView textview_hotspot_date;
		
	}

	final class HotspotItemViewHolder {

		private LinearLayout layout_comment_container;
    	
    	private TextView textview_news_url;
    	
    	private TextView textview_news_title;
    	
    	private TextView textview_comment_id;
    	
    	private TextView textview_comment_content;
    	
    	private TextView textview_comment_attention;
    	
    	private TextView textview_news_truth_degree;
    	
	}

	@Override
	public int getCount() {
		return this.fragment.getListItems().size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		if (position % 6 == 0) {
			return VIEW_TYPE_DATE_HEADER;
		} else {
			return VIEW_TYPE_HOTSPOT;
		}
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Map<String, Object> itemData = this.fragment.getListItems().get(position);
		Log.v(TAG, "当前行数据：" + itemData);
		DateHeaderViewHolder dateHeaderHolder = null;
		HotspotItemViewHolder hotspotHolder = null;
		int viewType = this.getItemViewType(position);
		if (null == convertView) {
			switch (viewType) {
			case VIEW_TYPE_DATE_HEADER:
				convertView = this.listContainer.inflate(R.layout.hotspot_item1, null);
				dateHeaderHolder = new DateHeaderViewHolder();
				dateHeaderHolder.textview_hotspot_date = (TextView)convertView.findViewById(R.id.textview_hotspot_date);
				convertView.setTag(dateHeaderHolder);
				break;
			case VIEW_TYPE_HOTSPOT:
				convertView = this.listContainer.inflate(R.layout.comment_item, null);
				hotspotHolder = new HotspotItemViewHolder();
				hotspotHolder.layout_comment_container = (LinearLayout)convertView.findViewById(R.id.layout_comment_container);
				hotspotHolder.textview_news_url = (TextView)convertView.findViewById(R.id.textview_news_url);
				hotspotHolder.textview_news_title = (TextView)convertView.findViewById(R.id.textview_news_title);
				hotspotHolder.textview_news_truth_degree = (TextView)convertView.findViewById(R.id.textview_news_truth_degree);
				hotspotHolder.textview_comment_id = (TextView)convertView.findViewById(R.id.textview_comment_id);
				hotspotHolder.textview_comment_content = (TextView)convertView.findViewById(R.id.textview_comment_content);
				hotspotHolder.textview_comment_attention = (TextView)convertView.findViewById(R.id.textview_comment_attention);
				convertView.setTag(hotspotHolder);
				break;
			default:
				break;
			}
		} else {
			switch (viewType) {
			case VIEW_TYPE_DATE_HEADER:
				dateHeaderHolder = (DateHeaderViewHolder) convertView.getTag();
				break;
			case VIEW_TYPE_HOTSPOT:
				hotspotHolder = (HotspotItemViewHolder) convertView.getTag();
				break;
			default:
				break;
			}
		}

		// 初始化UI
		switch (viewType) {
		case VIEW_TYPE_DATE_HEADER:
			String rankDayStr = (String)itemData.get("rankDay");
			dateHeaderHolder.textview_hotspot_date.setText(rankDayStr);
			break;
		case VIEW_TYPE_HOTSPOT:
			hotspotHolder.textview_news_url.setText(itemData.get("news_url").toString());
			hotspotHolder.textview_news_title.setText(itemData.get("news_title").toString());
			double news_truth_degree = Double.parseDouble(itemData.get("news_truth_degree").toString());
			DecimalFormat df = new DecimalFormat("###.00");
			String news_truth_degree_formated = df.format(news_truth_degree);
			hotspotHolder.textview_news_truth_degree.setText(news_truth_degree_formated);
			hotspotHolder.textview_comment_id.setText(itemData.get("comment_id").toString());
			hotspotHolder.textview_comment_content.setText(itemData.get("comment_content").toString());
			hotspotHolder.textview_comment_attention.setText(itemData.get("comment_attention").toString());
			int background = Integer.parseInt(itemData.get("background").toString());
			int alpha = Integer.parseInt(itemData.get("alpha").toString());
			hotspotHolder.layout_comment_container.setBackgroundResource(background);
			if(alpha != -1) {
				hotspotHolder.layout_comment_container.setAlpha(alpha);
			}
			break;
		default:
			break;
		}
		return convertView;
	}

}
