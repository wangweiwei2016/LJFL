package com.aimowei.Activity.garbage.engine;

public interface MessageManagementEngine {
	
	/**
	 * 获取消息列表
	 * @param username--接收人信息（社区用户发送社区用户名称，否则发送用户帐号）
	 * @param skipNum--跳过消息的条数
	 * @param takeNum--获取消息的条数
	 */
	public String queryMessage(String username, String skipNum, String takeNum);
	
	/**
	 * 删除消息
	 * @param serialnumber--消息流水号
	 */
	public String deleteMessage(String serialnumber);
}
