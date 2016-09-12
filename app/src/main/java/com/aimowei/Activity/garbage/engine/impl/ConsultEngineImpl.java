package com.aimowei.Activity.garbage.engine.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.engine.ConsultEngine;
import com.aimowei.Activity.garbage.engine.RankingListEngine.RankingModel;
import com.aimowei.Activity.garbage.net.HttpClientUtil;

public class ConsultEngineImpl implements ConsultEngine{

	/**错误信息*/
	public static String errorMsg = null ;
	/**是否成功,成功就不取错误信息分析了*/
	public static boolean isSucceed = false;
	@Override
	public String getConsult(String JYZXLX, String type, String faqineirong,
			String faqiren) {
		/*输入参数填充*/
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("JYZXLX", JYZXLX);
		jsonMap.put("type", type);
		jsonMap.put("faqineirong", faqineirong);
		jsonMap.put("faqiren", faqiren);
		
		HttpClientUtil util = new HttpClientUtil();
		/*发送请求*/
		String json = util.sendPost(ConstantValue.LOTTERY_URI.concat("Setting/AddconsultorSuggest"), jsonMap);
		
		
		// 3数据处理
				// 获取处理结果(状态+数据)
				// 将服务器传回来的数据进行解析--转换成类是Map的格式
				try {
					JSONObject object = new JSONObject(json);

					return object.getString("Info");
					// return "";
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// 4数据持久化

				return null;
	}
	/**
	 *获取建议咨询类别
	 */
	@Override
	public String[] getConsultType() {
		HttpClientUtil util = new HttpClientUtil();
		// 拼凑URL
		String url = ConstantValue.LOTTERY_URI.concat(
				QUERY_ACTION1_URL);
		// 发送请求
		String json = util.sendGet(url);
		isSucceed = false;// 初始化成功标志为否
		// 检测返回值
		if (json == null || json.isEmpty()) {
			errorMsg = Tip_NoReturn;
			return null;
		}
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
			String [] types = new String[length];
			
			for (int i = 0; i < length; i++) {//数据填充
				dataObj = datas.getJSONObject(i);
				types[i] = dataObj.getString("Name");
			}
			isSucceed = true;
			return types;

		} catch (JSONException e) {
			errorMsg = Tip_DataFormatError;
			e.printStackTrace();
			return null;
		}
	}

}
