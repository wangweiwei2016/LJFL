package com.aimowei.Activity.garbage.engine.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.engine.ApplyVolunteerEngine;
import com.aimowei.Activity.garbage.net.HttpClientUtil;

public class ApplyVolunteerEngineImpl implements ApplyVolunteerEngine {

	@Override
	public String getmessage(String sqdm, String skipNum, String takeNum) {
		// 设置要发送给服务器的参数
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Value1", sqdm);
		jsonMap.put("Value2", skipNum);
		jsonMap.put("Value3", takeNum);
		// 访问网络
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(ConstantValue.LOTTERY_URI.concat("ApplyVolunteer/Get"), jsonMap);
		// 获取处理结果(状态+数据)
		// 将服务器传回来的数据进行解析--转换成类是Map的格式，进行初步检查看是否有Status、Info、Data三个字段
		try {
			JSONObject object = new JSONObject(json);
			// 检查状态
			if (!object.has("Status")) {
				return null;
			}
			if (!object.has("Info")) {
				return null;
			}
			if (!object.has("Data")) {
				return null;
			}
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String apply(String yddh, String starttime, String endtime) {
		// 设置要发送给服务器的参数
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Phone", yddh);
		jsonMap.put("StartTime", starttime);
		jsonMap.put("EndTime", endtime);
		// 访问网络
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(ConstantValue.LOTTERY_URI.concat("ApplyVolunteer/Apply"), jsonMap);
		// 获取处理结果(状态)
		// 将服务器传回来的数据进行解析--取出"Info"的信息
		try {
			JSONObject object = new JSONObject(json);
			// 检查状态
			if (!object.has("Status")) {
				return null;
			}
			if (!object.has("Info")) {
				return null;
			}
			if (!object.has("Data")) {
				return null;
			}
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
