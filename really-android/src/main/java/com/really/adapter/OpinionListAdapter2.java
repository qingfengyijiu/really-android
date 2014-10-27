package com.really.adapter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.really.OpinionActivity2;
import com.really.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OpinionListAdapter2 extends BaseAdapter {

	private static final String TAG = OpinionListAdapter2.class.getName();

	private static final int VIEW_TYPE_COUNT = 2;

	private static final int VIEW_TYPE_COMMENT = 0;

	private static final int VIEW_TYPE_OPINION = 1;

	private List<Map<String, Object>> listItems; // 评论数据集合

	private LayoutInflater listContainer; // 视图容器

	private OpinionActivity2 opinionActivity;

	public OpinionListAdapter2(OpinionActivity2 opinionActivity, List<Map<String, Object>> listItems) {
		this.listItems = listItems;
		this.listContainer = LayoutInflater.from(opinionActivity);
		this.opinionActivity = opinionActivity;
	}

	final class CommentItemViewHolder {

		private LinearLayout layout_comment_container;

		private TextView textview_news_title;

		private TextView textview_news_truth_degree;

		private TextView textview_comment_content;

		private TextView textview_comment_attention;
		
		private ImageView imageview_news_link;

	}

	final class OpinionItemViewHolder {

		private ImageView imageview_opinion_icon;

		private TextView textview_opinion_content;

		private TextView textview_opinion_createtime;

		private TextView textview_opinion_id;

	}

	@Override
	public int getCount() {
		return this.listItems.size();
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
		if (position == 0) {
			return VIEW_TYPE_COMMENT;
		} else {
			return VIEW_TYPE_OPINION;
		}
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Map<String, Object> itemData = this.listItems.get(position);
		Log.v(TAG, "当前行数据:" + itemData);
		CommentItemViewHolder commentHolder = null;
		OpinionItemViewHolder opinionHolder = null;
		int viewType = this.getItemViewType(position);
		if (null == convertView) {
			switch (viewType) {
			case VIEW_TYPE_COMMENT:
				convertView = this.listContainer.inflate(R.layout.opinion_item1, null);
				commentHolder = new CommentItemViewHolder();
				commentHolder.layout_comment_container = (LinearLayout) convertView.findViewById(R.id.layout_comment_container);
				commentHolder.textview_news_title = (TextView) convertView.findViewById(R.id.textview_news_title);
				commentHolder.textview_news_truth_degree = (TextView) convertView.findViewById(R.id.textview_news_truth_degree);
				commentHolder.textview_comment_content = (TextView) convertView.findViewById(R.id.textview_comment_content);
				commentHolder.textview_comment_attention = (TextView) convertView.findViewById(R.id.textview_comment_attention);
				commentHolder.imageview_news_link = (ImageView)convertView.findViewById(R.id.imageview_news_link);
				convertView.setTag(commentHolder);
				break;
			case VIEW_TYPE_OPINION:
				convertView = this.listContainer.inflate(R.layout.opinion_item2, null);
				opinionHolder = new OpinionItemViewHolder();
				opinionHolder.imageview_opinion_icon = (ImageView) convertView.findViewById(R.id.imageview_opinion_icon);
				opinionHolder.textview_opinion_content = (TextView) convertView.findViewById(R.id.textview_opinion_content);
				opinionHolder.textview_opinion_createtime = (TextView) convertView.findViewById(R.id.textview_opinion_createtime);
				opinionHolder.textview_opinion_id = (TextView) convertView.findViewById(R.id.textview_opinion_id);
				convertView.setTag(opinionHolder);
				break;
			default:
				break;
			}
		} else {
			switch (viewType) {
			case VIEW_TYPE_COMMENT:
				commentHolder = (CommentItemViewHolder) convertView.getTag();
				break;
			case VIEW_TYPE_OPINION:
				opinionHolder = (OpinionItemViewHolder) convertView.getTag();
				break;
			default:
				break;
			}
		}

		// 初始化UI
		switch (viewType) {
		case VIEW_TYPE_COMMENT:
			commentHolder.textview_news_title.setText(this.opinionActivity.getNews_title());
			commentHolder.textview_news_truth_degree.setText(this.opinionActivity.getNews_truth_degree());
			commentHolder.textview_comment_content.setText(this.opinionActivity.getComment_content());
			commentHolder.textview_comment_attention.setText(this.opinionActivity.getComment_attention());
			commentHolder.layout_comment_container.setBackgroundResource(this.opinionActivity.getBackground());
			int alpha = this.opinionActivity.getAlpha();
			if (alpha != -1) {
				commentHolder.layout_comment_container.setAlpha(alpha);
			}

			// 为页面元素增加监听器
			commentHolder.textview_comment_content.setOnClickListener(this.opinionActivity);
			commentHolder.textview_news_title.setOnClickListener(this.opinionActivity);
			commentHolder.imageview_news_link.setOnClickListener(this.opinionActivity);
			break;
		case VIEW_TYPE_OPINION:
			String opinion_id = itemData.get("opinion_id").toString();
			opinionHolder.textview_opinion_id.setText(opinion_id);
			String opinion_content = (String) itemData.get("opinion_content");
			opinionHolder.textview_opinion_content.setText(opinion_content);
			Date opinion_createTime = new Date((Long) itemData.get("opinion_createTime"));
			String timeTip = this._getTimeTip(opinion_createTime);
			opinionHolder.textview_opinion_createtime.setText(timeTip);
			int truthDegree = Integer.parseInt(itemData.get("truthDegree").toString());
			this._setTruthDegreeIcon(opinionHolder.imageview_opinion_icon, truthDegree);
			break;
		default:
			break;
		}
		return convertView;
	}

	private void _setTruthDegreeIcon(ImageView view, int truthDegree) {
		switch (truthDegree) {
		case 1:
			view.setBackgroundResource(R.drawable.opinion_fake_selected);
			break;
		case 2:
			view.setBackgroundResource(R.drawable.opinion_dubious_selected);
			break;
		case 3:
			view.setBackgroundResource(R.drawable.opinion_indifferent_selected);
			break;
		case 4:
			view.setBackgroundResource(R.drawable.opinion_credible_selected);
			break;
		case 5:
			view.setBackgroundResource(R.drawable.opinion_believable_selected);
			break;
		default:
			view.setBackgroundResource(R.drawable.news_comment_indifferent);
			break;
		}
	}

	private String _getTimeTip(Date createTime) {
		String tip;
		Date now = new Date();
		long diff = now.getTime() - createTime.getTime();
		diff = diff / 1000;
		if (diff < 1) {
			diff = 1;
		}
		if (diff < 60) {
			tip = diff + "秒前";
		} else if (diff < 60 * 60) {
			diff = diff / 60;
			tip = diff + "分钟前";
		} else if (diff < 60 * 60 * 24) {
			diff = diff / (60 * 60);
			tip = diff + "小时前";
		} else if (diff < 60 * 60 * 24 * 7) {
			diff = diff / (60 * 60 * 24);
			tip = diff + "天前";
		} else if (diff < 60 * 60 * 24 * 31) {
			diff = diff / (60 * 60 * 24 * 7);
			tip = diff + "周前";
		} else if (diff < 60 * 60 * 24 * 365) {
			diff = diff / (60 * 60 * 24 * 31);
			tip = diff + "月前";
		} else {
			diff = diff / (60 * 60 * 24 * 365);
			tip = diff + "年前";
		}
		return tip;
	}

}
