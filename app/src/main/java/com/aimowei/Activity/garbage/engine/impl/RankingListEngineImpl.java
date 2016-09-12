package com.aimowei.Activity.garbage.engine.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.engine.RankingListEngine;
import com.aimowei.Activity.garbage.net.HttpClientUtil;

/**
 * 排行榜查询接口实现类
 * 
 * @author SWZ
 */
public class RankingListEngineImpl implements RankingListEngine {
	/** 错误信息 */
	public static String errorMsg = null;
	/** 是否成功,成功就不取错误信息分析了 */
	public static boolean isSucceed = false;

	@Override
	public LinkedList<RankingModel> queryRankingList(String account,String SQDM,String queryType) {

		HttpClientUtil util = new HttpClientUtil();
		// 拼凑URL
		String url = ConstantValue.LOTTERY_URI.concat(
				URL_Controller_Information).concat(QUERY_ACTION1_URL);
		// 填充参数
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Value1", account);
		jsonMap.put("Value2", SQDM);
		jsonMap.put("Value3", queryType);
		// 发送请求
		String json = util.sendPost(url, jsonMap);
		
		isSucceed = false;// 初始化成功标志为否
		// 检测返回值
		if (json == null || json.isEmpty()) {
			errorMsg = Tip_NoReturn;
			return null;
		}
		Log.e("json", json);
		// 将服务器传回来的数据进行解析--转换成类是Map的格式
		try {
			JSONObject topObject = new JSONObject(json);
			if (!topObject.has(KeyWord_Status)) {// 是否有状态
				errorMsg = Tip_LostStatus;
				return null;
			}
			int status = topObject.getInt(KeyWord_Status);
			if (topObject.has(KeyWord_Info)) {// 是否有信息
				errorMsg = topObject.getString(KeyWord_Info);
			} else {
				errorMsg = Tip_LostInfo;
			}
			switch (status)// 判断状态
			{
			case Status_Fail:
				return null;
			case Status_Succeed:
				break;
			case Status_DataEmpty:
				isSucceed = true;
				return null;
			default:
				errorMsg = Tip_UnknownStatus;
				return null;
			}
			if (!topObject.has(KeyWord_Datas)) {//是否有数据
				errorMsg = Tip_LostDatas;
				return null;
			}
			JSONArray datas = topObject.getJSONArray(KeyWord_Datas);
			int length = datas.length();
			JSONObject dataObj = null;
			if (length <= 0) {//判断是否有数据(极端情况,前面状态说有数据但是这里却没有数据??不大可能发生,发生则为后台出错)
				errorMsg = Tip_DataEmpty;
				return null;
			}
			if (length > 10) // 强制转化数量
				length = 10;
			LinkedList<RankingModel> rankings = new LinkedList<RankingModel>();
			RankingModel tmpModel = null;
			for (int i = 0; i < length; i++) {//数据填充
				dataObj = datas.getJSONObject(i);
				tmpModel = new RankingModel();
				tmpModel.ranking = dataObj.getString("Ranking");
				tmpModel.integral = dataObj.getString("Integral");
				tmpModel.name = dataObj.getString("SQYHMC");
				rankings.addLast(tmpModel);
			}
			isSucceed = true;
			return rankings;

		} catch (JSONException e) {
			errorMsg = Tip_DataFormatError;
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public SelfRankingModel queryMyRanking(String MC, String queryType) {

		HttpClientUtil util = new HttpClientUtil();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Value1", MC);
		jsonMap.put("Value2", queryType);
		String url = ConstantValue.LOTTERY_URI.concat(
				URL_Controller_Information).concat(QUERY_ACTION2_URL);
		String json = util.sendPost(url, jsonMap);
		isSucceed = false;
		if (json == null || json.isEmpty()) {
			errorMsg = Tip_NoReturn;
			return null;
		}
		// 将服务器传回来的数据进行解析--转换成类是Map的格式
		try {
			JSONObject topObject = new JSONObject(json);
			if (!topObject.has(KeyWord_Status)) {
				errorMsg = Tip_LostStatus;
				return null;
			}
			int status = topObject.getInt(KeyWord_Status);
			if (topObject.has(KeyWord_Info)) {
				errorMsg = topObject.getString(KeyWord_Info);
			} else {
				errorMsg = Tip_LostInfo;
			}
			switch (status) {
			case Status_Fail:
				return null;
			case Status_Succeed:
				break;
			case Status_DataEmpty:
				isSucceed = true;
				return null;
			default:
				errorMsg = Tip_UnknownStatus;
				return null;
			}
			if (!topObject.has(KeyWord_Datas)) {
				errorMsg = Tip_LostDatas;
				return null;
			}
			topObject = topObject.getJSONObject(KeyWord_Datas);
			SelfRankingModel model = new SelfRankingModel();
			model.ranking = topObject.getString("Ranking");
			model.integral = topObject.getString("Integral");
			model.putquantity = topObject.getString("Putquantity");
			return model;
		} catch (JSONException e) {
			errorMsg = Tip_DataFormatError;
			e.printStackTrace();
			return null;
		}
	}
}
