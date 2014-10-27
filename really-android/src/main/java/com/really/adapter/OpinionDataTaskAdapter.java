package com.really.adapter;

import android.content.Context;

import com.really.service.ServerService;

public class OpinionDataTaskAdapter extends DataTaskAdapter {
	
	private long commentId;
	
	public OpinionDataTaskAdapter(Context ctx, long commentId, int pageCount) {
		super(ctx);
		this.commentId = commentId;
		this.setPageCount(pageCount);
	}
	
	public String[] getData() {
		ServerService serverService = new ServerService(this.getCtx());
		String[] data = serverService.getOpinions(this.commentId, this.getPageCount());
		
		return data;
	}

}
