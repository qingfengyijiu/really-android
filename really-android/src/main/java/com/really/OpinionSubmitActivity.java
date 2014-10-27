package com.really;

import java.util.HashMap;
import java.util.Map;

import com.really.enumeration.Relationship;
import com.really.enumeration.TruthDegree;
import com.really.exception.SystemException;
import com.really.service.ErrorMessageHelper;
import com.really.service.SecurityContextUtils;
import com.really.service.ServerService;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OpinionSubmitActivity extends Activity implements OnClickListener {
	
	private ActionBar actionBar;
	
	private long comment_id;
	
	private int truthDegree;
	
	private int relationship;
	
	private ImageView imageview_opinion_fake;
	
	private ImageView imageview_opinion_dubious;
	
	private ImageView imageview_opinion_indifferent;
	
	private ImageView imageview_opinion_credible;
	
	private ImageView imageview_opinion_believable;
	
	private View layout_comment_relationship;
	
	private TextView textview_relitionship_litigant;
	
	private TextView textview_relitionship_insider;
	
	private TextView textview_relitionship_party;
	
	private TextView textview_relitionship_industry;
	
	private EditText edittext_opinion_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opinion_submit);
		Intent intent = this.getIntent();
		this.comment_id = intent.getLongExtra("comment_id", -1);
		this._initView();
	}
	
	private void _initView() {
		// 初始化actionBar
		actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = inflator.inflate(R.layout.activity_opinion_submit_header, null);
	    ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    actionBar.setCustomView(view, layout);
		actionBar.show();
		ImageButton imagebutton_back = (ImageButton)view.findViewById(R.id.imagebutton_opinion_submit_back);
		imagebutton_back.setOnClickListener(this);
		ImageButton imagebutton_send = (ImageButton)view.findViewById(R.id.imagebutton_opinion_submit_send);
		imagebutton_send.setOnClickListener(this);
		//获取页面元素
		this.imageview_opinion_fake = (ImageView)this.findViewById(R.id.imageview_opinion_fake);
		this.imageview_opinion_dubious = (ImageView)this.findViewById(R.id.imageview_opinion_dubious);
		this.imageview_opinion_indifferent = (ImageView)this.findViewById(R.id.imageview_opinion_indifferent);
		this.imageview_opinion_credible = (ImageView)this.findViewById(R.id.imageview_opinion_credible);
		this.imageview_opinion_believable = (ImageView)this.findViewById(R.id.imageview_opinion_believable);
		this.textview_relitionship_litigant = (TextView)this.findViewById(R.id.textview_relationship_litigant);
		this.textview_relitionship_insider = (TextView)this.findViewById(R.id.textview_relationship_insider);
		this.textview_relitionship_party = (TextView)this.findViewById(R.id.textview_relationship_party);
		this.textview_relitionship_industry = (TextView)this.findViewById(R.id.textview_relationship_industry);
		this.edittext_opinion_content = (EditText)this.findViewById(R.id.edittext_opinion_content);
		
		//为页面元素注册事件
		this.imageview_opinion_fake.setOnClickListener(this);
		this.imageview_opinion_dubious.setOnClickListener(this);
		this.imageview_opinion_indifferent.setOnClickListener(this);
		this.imageview_opinion_credible.setOnClickListener(this);
		this.imageview_opinion_believable.setOnClickListener(this);
		this.textview_relitionship_litigant.setOnClickListener(this);
		this.textview_relitionship_insider.setOnClickListener(this);
		this.textview_relitionship_party.setOnClickListener(this);
		this.textview_relitionship_industry.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id) {
		case R.id.imagebutton_opinion_submit_back :
			this.finish();
			break;
		case R.id.imagebutton_opinion_submit_send :
			_sendOpinion();
			break;
		case R.id.imageview_opinion_fake :
		case R.id.imageview_opinion_dubious :
		case R.id.imageview_opinion_indifferent :
		case R.id.imageview_opinion_credible :
		case R.id.imageview_opinion_believable :
			_chooseTruthDegree(id);
			break;
		case R.id.textview_relationship_litigant :
		case R.id.textview_relationship_insider :
		case R.id.textview_relationship_party :
		case R.id.textview_relationship_industry :
			_chooseRelationship(id);
			break;
		default :
			break;
		}
		
	}
	
	private void _sendOpinion() {
		String opinion_content = this.edittext_opinion_content.getText().toString();
		if(this.truthDegree == 0) {
			Toast.makeText(this, "请选择可信度", Toast.LENGTH_SHORT).show();
			return;
		}
		if(this.relationship == 0 && (this.truthDegree == TruthDegree.FAKE || this.truthDegree == TruthDegree.BELIEVABLE)) {
			Toast.makeText(this, "请选择事件身份", Toast.LENGTH_SHORT).show();
			return;
		}
		if(opinion_content == null || opinion_content.length() == 0) {
			Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
			this.edittext_opinion_content.requestFocus();
			return;
		}
		long userId = SecurityContextUtils.getCurrentUser().getId();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("commentId", this.comment_id);
		data.put("userId", userId);
		data.put("truthDegree", this.truthDegree);
		data.put("relationship", this.relationship);
		data.put("content", opinion_content);
		ServerService serverService = new ServerService(this);
		try {
			serverService.submitOpinion(data);
		} catch (SystemException e) {
			ErrorMessageHelper.showErrorMessage(this, e);
			return;
		}
		this.finish();
	}
	
	private void _chooseTruthDegree(int id) {
		if(SecurityContextUtils.checkhasLoginAndAutoLogin(this)) {
			return;
		}
		switch(id) {
			case R.id.imageview_opinion_fake :
				this.truthDegree = TruthDegree.FAKE;
				this.imageview_opinion_fake.setBackgroundResource(R.drawable.opinion_fake_selected);
				this.imageview_opinion_dubious.setBackgroundResource(R.drawable.opinion_dubious_unselected);
				this.imageview_opinion_indifferent.setBackgroundResource(R.drawable.opinion_indifferent_unselected);
				this.imageview_opinion_credible.setBackgroundResource(R.drawable.opinion_credible_unselected);
				this.imageview_opinion_believable.setBackgroundResource(R.drawable.opinion_believable_unselected);
//				this.layout_comment_relationship.setVisibility(View.VISIBLE);
				break;
			case R.id.imageview_opinion_dubious :
				this.truthDegree = TruthDegree.DUBIOUS;
				this.imageview_opinion_fake.setBackgroundResource(R.drawable.opinion_fake_selected);
				this.imageview_opinion_dubious.setBackgroundResource(R.drawable.opinion_dubious_selected);
				this.imageview_opinion_indifferent.setBackgroundResource(R.drawable.opinion_indifferent_unselected);
				this.imageview_opinion_credible.setBackgroundResource(R.drawable.opinion_credible_unselected);
				this.imageview_opinion_believable.setBackgroundResource(R.drawable.opinion_believable_unselected);
//				this.layout_comment_comment.setVisibility(View.VISIBLE);
//				this.edittext_comment_comment.requestFocus();
				break;
			case R.id.imageview_opinion_indifferent :
				this.truthDegree = TruthDegree.INDIFFERENT;
				this.imageview_opinion_fake.setBackgroundResource(R.drawable.opinion_fake_selected);
				this.imageview_opinion_dubious.setBackgroundResource(R.drawable.opinion_dubious_selected);
				this.imageview_opinion_indifferent.setBackgroundResource(R.drawable.opinion_indifferent_selected);
				this.imageview_opinion_credible.setBackgroundResource(R.drawable.opinion_credible_unselected);
				this.imageview_opinion_believable.setBackgroundResource(R.drawable.opinion_believable_unselected);
//				this.layout_comment_comment.setVisibility(View.VISIBLE);
//				this.edittext_comment_comment.requestFocus();
				break;
			case R.id.imageview_opinion_credible :
				this.truthDegree = TruthDegree.CREDIBLE;
				this.imageview_opinion_fake.setBackgroundResource(R.drawable.opinion_fake_selected);
				this.imageview_opinion_dubious.setBackgroundResource(R.drawable.opinion_dubious_selected);
				this.imageview_opinion_indifferent.setBackgroundResource(R.drawable.opinion_indifferent_selected);
				this.imageview_opinion_credible.setBackgroundResource(R.drawable.opinion_credible_selected);
				this.imageview_opinion_believable.setBackgroundResource(R.drawable.opinion_believable_unselected);
//				this.layout_comment_comment.setVisibility(View.VISIBLE);
//				this.edittext_comment_comment.requestFocus();
				break;
			case R.id.imageview_opinion_believable :
				this.truthDegree = TruthDegree.BELIEVABLE;
				this.imageview_opinion_fake.setBackgroundResource(R.drawable.opinion_fake_selected);
				this.imageview_opinion_dubious.setBackgroundResource(R.drawable.opinion_dubious_selected);
				this.imageview_opinion_indifferent.setBackgroundResource(R.drawable.opinion_indifferent_selected);
				this.imageview_opinion_credible.setBackgroundResource(R.drawable.opinion_credible_selected);
				this.imageview_opinion_believable.setBackgroundResource(R.drawable.opinion_believable_selected);
//				this.layout_comment_relationship.setVisibility(View.VISIBLE);
				break;
			default : 
				break;
		}
	}
	
	private void _chooseRelationship(int id) {
		_clearRelationshipView();
		switch(id) {
			case R.id.textview_relationship_litigant :
				this.relationship = Relationship.LITIGANT;
				this.textview_relitionship_litigant.setBackgroundResource(R.drawable.opinion_relationship_selected);
				break;
			case R.id.textview_relationship_insider :
				this.relationship = Relationship.INSIDER;
				this.textview_relitionship_insider.setBackgroundResource(R.drawable.opinion_relationship_selected);
				break;
			case R.id.textview_relationship_party :
				this.relationship = Relationship.PARTY;
				this.textview_relitionship_party.setBackgroundResource(R.drawable.opinion_relationship_selected);
				break;
			case R.id.textview_relationship_industry :
				this.relationship = Relationship.INDUSTRY;
				this.textview_relitionship_industry.setBackgroundResource(R.drawable.opinion_relationship_selected);
				break;
			default :
				break;
		}
//		this.layout_comment_relationship.setVisibility(View.GONE);
//		this.layout_comment_comment.setVisibility(View.VISIBLE);
//		this.edittext_comment_comment.requestFocus();
	}
	
	private void _clearRelationshipView() {
		this.textview_relitionship_litigant.setBackgroundResource(R.drawable.opinion_relationship_unselected);
		this.textview_relitionship_insider.setBackgroundResource(R.drawable.opinion_relationship_unselected);
		this.textview_relitionship_party.setBackgroundResource(R.drawable.opinion_relationship_unselected);
		this.textview_relitionship_industry.setBackgroundResource(R.drawable.opinion_relationship_unselected);
	}
	
}
