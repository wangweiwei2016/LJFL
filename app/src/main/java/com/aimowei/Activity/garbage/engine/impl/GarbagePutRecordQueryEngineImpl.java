package com.aimowei.Activity.garbage.engine.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.format.DateFormat;

import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.engine.EventArrangementEngine;
import com.aimowei.Activity.garbage.engine.GarbagePutRecordQueryEngine;
import com.aimowei.Activity.garbage.net.HttpClientUtil;

/**
 * 垃圾投放记录查询接口实现类
 * 
 * @author SWZ
 */
public class GarbagePutRecordQueryEngineImpl implements
		GarbagePutRecordQueryEngine {
	/** 错误信息 */
	public static String errorMsg = null;
	/** 是否成功,成功就不取错误信息分析了 */
	public static boolean isSucceed = false;

	@Override
	public LinkedList<GarbagePutRecordInfoModel> queryGarbagePutRecordInfo(
			String YHZH, Calendar beginDate, Calendar endDate) {
		HttpClientUtil util = new HttpClientUtil();
		// 拼凑URL
		String url = ConstantValue.LOTTERY_URI.concat(
				EventArrangementEngine.URL_Controller_Information).concat(
				QUERY_ACTION1_URL);
		// 填充参数
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Value1", YHZH);
		jsonMap.put("Value2",
				DateFormat.format("yyyy-MM-dd", beginDate.getTime()).toString());
		jsonMap.put("Value3", DateFormat
				.format("yyyy-MM-dd", endDate.getTime()).toString());
		// 发送请求
		String json = util.sendPost(url, jsonMap);
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
			LinkedList<GarbagePutRecordInfoModel> infos = new LinkedList<GarbagePutRecordInfoModel>();
			GarbagePutRecordInfoModel tmpModel = null;
			for (int i = 0; i < length; i++) {//数据填充
				dataObj = datas.getJSONObject(i);
				tmpModel = new GarbagePutRecordInfoModel();
				tmpModel.time = dataObj.getString("Time");
				tmpModel.address = dataObj.getString("Address");
				tmpModel.weight = dataObj.getString("Weight");
				tmpModel.integral = dataObj.getString("Integral");
				infos.addLast(tmpModel);
			}
			isSucceed = true;
			return infos;

		} catch (Exception e) {
			errorMsg = Tip_DataFormatError;
			e.printStackTrace();
			return null;
		}
	}
}