package com.aimowei.Activity.garbage.engine;

public interface ApplyVolunteerEngine {

	/**
	 * 获取申请消息
	 * @param sqdm--社区代码
	 * @param skipNum--跳过消息的条数
	 * @param takeNum--获取消息的条数
	 */
	public String getmessage(String sqdm,String skipNum,String takeNum);
	
	/**
	 * 申请志愿者
	 * @param yddh--移动电话
	 * @param starttime--开始时间
	 * @param endtime--结束时间
	 */
	public String apply(String yddh,String starttime,String endtime);
}
