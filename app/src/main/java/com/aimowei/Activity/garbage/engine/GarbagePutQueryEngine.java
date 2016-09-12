package com.aimowei.Activity.garbage.engine;

import java.util.Calendar;
import java.util.LinkedList;
/**
 * 垃圾投放查询引擎接口,继承自查询共享引擎
 * @author SWZ
 */
public interface GarbagePutQueryEngine extends QueryShareEngine{
	/**Action 垃圾投放信息查询URL*/
	public static final String QUERY_ACTION2_URL= "/GetGarbagePutInfoWithLimit";
	/**垃圾投放信息Model(简单结构体)*/
	public class GarbagePutInfoModel
	{
		/**
		 * 时间
		 */
		public String time ;
		/**
		 * 地点
		 */
		public String address ;
		/**
		 * 管理员
		 */
		public String manager;
		/**
		 * 图片URL
		 */
		public String pic;
	}
	/**
	 * 联网查询活动列表数据,使用前请检查网络,且不在主线程操作
	 * @param account 帐号
	 * @param queryDate 查询的日期
	 * @param nextIndex 最小索引
	 * @param maxLength 长度
	 * @return 垃圾投放信息Model链表,null为失败
	 */
	public LinkedList<GarbagePutInfoModel> queryGarbagePutInfo(String account,
			Calendar queryDate,int nextIndex,int maxLength);
}
