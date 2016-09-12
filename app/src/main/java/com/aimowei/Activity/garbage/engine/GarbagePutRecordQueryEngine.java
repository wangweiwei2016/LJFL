package com.aimowei.Activity.garbage.engine;

import java.util.Calendar;
import java.util.LinkedList;
/**
 * 垃圾投放记录引擎接口,继承自查询共享引擎
 * @author SWZ
 */
public interface GarbagePutRecordQueryEngine extends QueryShareEngine{
	/**Action 垃圾投放记录信息查询URL*/
	public static final String QUERY_ACTION1_URL= "/GetGarbagePutRecordWithLimit";
	/**垃圾投放记录Model*/
	public class GarbagePutRecordInfoModel
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
		 * 投放量
		 */
		public String weight;
		/**
		 * 积分
		 */
		public String integral;
	}
	/**
	 * 联网查询垃圾投放记录数据,使用前请检查网络,且不在主线程操作
	 * @param YHSQMC 用户社区名称
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 *  @return 垃圾投放记录Model链表,null为失败
	 */
	public LinkedList<GarbagePutRecordInfoModel> queryGarbagePutRecordInfo(String YHSQMC,Calendar beginDate,Calendar endDate);
}