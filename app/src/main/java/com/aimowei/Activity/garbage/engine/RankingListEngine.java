package com.aimowei.Activity.garbage.engine;

import java.util.LinkedList;
/**
 * 垃圾投放排行查询引擎接口,继承自查询共享引擎
 * @author SWZ
 */
public interface RankingListEngine extends QueryShareEngine {
	/** Action 获取排行信息URL */
	public static final String QUERY_ACTION1_URL = "/GetRankingList";
	/** Action 获取我的排行信息URL */
	public static final String QUERY_ACTION2_URL = "/GetMyRanking";
	/** 排行榜信息Model */
	public class SelfRankingModel {
		/**
		 * 排名
		 */
		public String ranking;
		/**
		 * 积分
		 */
		public String integral;
		/**
		 * 投放量
		 */
		public String putquantity;
	}
	/** 排行榜信息Model */
	public class RankingModel {
		/**
		 * 排名
		 */
		public String ranking;
		/**
		 * 积分
		 */
		public String integral;
		/**
		 * 社区用户名称
		 */
		public String name;
	}
	/**
	 * 联网查询排行榜数据,使用前请检查网络,且不在主线程操作
	 * @param account 帐号
	 * @param SQDM 社区代码
	 * @param queryType 排行类型 0-街道办 1-社区 2-小区 3-住宅楼
	 * @return 排行model链表
	 */
	public LinkedList<RankingModel> queryRankingList(String account,String SQDM,String queryType);
	/**
	 * 联网查询我的排行榜数据,使用前请检查网络,且不在主线程操作
	 * @param queryType 0-街道办 1- 社区  2- 小区 3-楼 
	 * @param SQYHMC
	 * @return SelfRankingModel
	 */
	public SelfRankingModel queryMyRanking(String MC, String queryType);
}
