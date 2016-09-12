package com.aimowei.Activity.garbage.engine;

public interface ShoppingEngine {
	/**
	 * 查询商品信息
	 * @param phone--移动电话
	 * @param type--（0表示按销量排序，1表示按积分排序）
	 * @param sort--（0表示降序，1表示升序）
	 * @param skipNum--跳过消息的条数
	 * @param takeNum--获取消息的条数
	 */
	public String queryCommodity(String phone, String type, String sort, String skipNum, String takeNum);
}
