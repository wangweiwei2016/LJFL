package com.aimowei.Activity.garbage.engine;

import java.util.LinkedList;
/**
 * 活动查询引擎接口,继承自查询共享引擎
 * @author SWZ
 */
public interface EventArrangementEngine extends QueryShareEngine{
	/**Action 获取活动消息详情URL*/
	public static final String QUERY_ACTION2_URL = "/GetActivityDetail";
	/**Action 获取社区消息详情URL*/
	public static final String QUERY_ACTION3_URL = "/GetCommunityInfos";
	/**Action 获取活动消息列表URL*/
	public static final String QUERY_ACTION4_URL= "/GetActivityListWithLimit";
	/**活动安排Model(简单结构体)*/
	public class EventArrangementModel
	{
		/**活动ID,方便引用*/
		public String id;
		/**活动开始时间*/
		public String startTime ;
		/**活动结束时间*/
		public String endTime ;
		/**活动类型名称*/
		public String type;
		/**活动主题*/
		public String title;
		/**活动详情*/
		public String detail;
	}
	/**
	 * 社区信息Model
	 */
	public class CommunityInfoModel
	{
		/**
		 * 社区ID(代码)
		 */
		public String id;
		/**
		 * 社区名称
		 */
		public String name;
	}
	/**
	 * 联网查询活动列表数据,使用前请检查网络,且不在主线程操作
	 * @param SQDM 社区代码
	 * @param minIndex 最小索引(跳过的数)
	 * @param length (最大接收长度)
	 * @return 活动信心Model链表
	 */
	public LinkedList<EventArrangementModel> queryActivityList(String SQDM,int minIndex,int length);
	/**
	 * 联网查询活动详细信息数据,使用前请检查网络,且不在主线程操作
	 * @param activityID 活动ID
	 * @return 活动信息链表,null为失败
	 */
	public String queryActivityDetail(String activityID);
}
