package com.aimowei.Activity.garbage.engine;

public interface OnRefreshListener {

	/**
	 * 下拉刷新    
	 * 每次下拉刷新都将重新获取消息记录并覆盖之前加载出来的listview
	 */
	void onDownPullRefresh();

	/**
	 * 上拉加载更多
	 */
	void onLoadingMore();
}