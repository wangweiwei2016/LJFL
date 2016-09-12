package com.aimowei.Activity.garbage.engine;

import java.util.LinkedList;
/**
 * 垃圾分类查询引擎接口,继承自查询共享引擎
 * @author SWZ
 */
public interface GarbageSortingEngine extends QueryShareEngine{
	/**Action 获取垃圾信息动作URL*/
	public static final String QUERY_ACTION1_URL = "/GetGarbageInfo";
	/**Action 获取垃圾类别信息动作URL*/
	public static final String QUERY_ACTION2_URL = "/GetGarbageCategoryInfo";
	/**
	 * 垃圾类别信息Model
	 * @author Administrator
	 */
	class GarbageSortingModel {
		/**
		 * 垃圾类别ID
		 */
		public int id;
		/**
		 * 垃圾类别名称
		 */
		public String name;
		/**
		 * 垃圾信息链表
		 */
		public LinkedList<GarbageModel> garbages = new LinkedList<GarbageModel>();
		/**使用指定的垃圾类别ID和名称初始化垃圾类别Model
		 * */
		public GarbageSortingModel(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	/**
	 * 垃圾信息Model
	 */
	class GarbageModel {
		/**垃圾ID*/
		public String id;
		/**垃圾名称*/
		public String name;
		/**垃圾积分*/
		public String integral;
		/**使用指定的垃圾ID和名称以及积分初始化垃圾类别Model
		 * */
		public GarbageModel(String id, String name,String integral) {
			this.id = id;
			this.name = name;
			this.integral = integral;
		}
	}
	/**
	 * 联网查询垃圾数据,使用前请检查网络,且不在主线程操作
	 * @param garbageCategoryID 垃圾类别ID
	 * @return 垃圾信息链表,null为失败
	 */
	public LinkedList<GarbageModel> queryGarbageInfo(int garbageCategoryID);
	/**
	 * 联网查询垃圾类别数据,使用前请检查网络,且不在主线程操作
	 * @return 垃圾类别信息链表,null为失败
	 */
	public LinkedList<GarbageSortingModel> queryGarbageCategoryInfo();
}
