package com.aimowei.Activity.garbage.engine.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.engine.ShoppingEngine;
import com.aimowei.Activity.garbage.net.HttpClientUtil;

public class ShoppingEngineImpl implements ShoppingEngine {

	@Override
	public String queryCommodity(String phone, String type, String sort, String skipNum, String takeNum) {

		// 设置要发送给服务器的参数
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Value1", phone);
		jsonMap.put("Value2", type);
		jsonMap.put("Value3", sort);
		jsonMap.put("Value4", skipNum);
		jsonMap.put("Value5", takeNum);
		Log.e("Value1", phone);
		Log.e("Value2", type);
		Log.e("Value3", sort);
		Log.e("Value4", skipNum);
		Log.e("Value5", takeNum);
		// 访问网络
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(ConstantValue.LOTTERY_URI
				.concat("IntegralMall/GetCommodityList"), jsonMap);
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

}
