package com.really.adapter;

import android.content.Context;

import com.really.service.ServerService;

public class HotspotDataTaskAdapter extends DataTaskAdapter {
	
	private int queryZone;
	
	public HotspotDataTaskAdapter(Context ctx, int queryZone,int pageCount) {
		super(ctx);
		this.queryZone = queryZone;
		this.setPageCount(pageCount);
	}
	
	public String[] getData() {
		ServerService serverService = new ServerService(this.getCtx());
		String[] data = serverService.getComments(this.queryZone, this.getPageCount());
		
		return data;
	}
}
