package com.really.adapter;

import android.content.Context;

public abstract class DataTaskAdapter {
	
	private Context ctx;
	
	private static final int DEFAULT_PAGE_COUNT = 2;
	
	private int pageCount = DEFAULT_PAGE_COUNT;
	
	public DataTaskAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public abstract String[] getData();

	public Context getCtx() {
		return ctx;
	}

	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
}
