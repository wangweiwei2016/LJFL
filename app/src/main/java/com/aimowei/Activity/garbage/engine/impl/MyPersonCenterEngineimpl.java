package com.aimowei.Activity.garbage.engine.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.engine.MyPersonCenterEngine;
import com.aimowei.Activity.garbage.net.HttpClientUtil;

public class MyPersonCenterEngineimpl implements MyPersonCenterEngine {

	@Override
	public String updatePassword(String user, String password,
			String newpassword) {
		// 联网获取数据
		// 1.设置参数
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		jsonMap.put("username", user);
		jsonMap.put("password", password);
		jsonMap.put("newpassword", newpassword);
		// 界面收集
		// 数据库获取+配置文件
		// 2.访问网络
		HttpClientUtil util = new HttpClientUtil();
		String json = util.sendPost(ConstantValue.LOTTERY_URI
				.concat("PersonalCenter/UpdatePassword"), jsonMap);
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

	@Override
	public String getCredit(String yonghuleibie,String phone) {
		// 联网获取数据
		// 1.设置参数
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		jsonMap.put("phone", phone);
		jsonMap.put("yonghuleibie", yonghuleibie);
		// 界面收集
		// 数据库获取+配置文件
		// 2.访问网络
		HttpClientUtil util = new HttpClientUtil();
		String json = util
				.sendPost(ConstantValue.LOTTERY_URI
						.concat("PersonalCenter/GetNowCredit"), jsonMap);
		// 3数据处理
		// 获取处理结果(状态+数据)
		// 将服务器传回来的数据进行解析--转换成类是Map的格式

		return json;

	}

	public String UpdateUserInfo(String yonghuleibie,String phone, String newphone, String YX) {
		// 联网获取数据
				// 1.设置参数
				Map<String, Object> jsonMap = new HashMap<String, Object>();

				jsonMap.put("yonghuleibie",yonghuleibie);
				jsonMap.put("phone", phone);
				jsonMap.put("newphone", newphone);
				jsonMap.put("newYX",YX);
				// 界面收集
				// 数据库获取+配置文件
				// 2.访问网络
				HttpClientUtil util = new HttpClientUtil();
				String json = util.sendPost(ConstantValue.LOTTERY_URI
						.concat("PersonalCenter/UpdateInfo"), jsonMap);
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

	@Override
	public String addLajiToufang(String SQYHMC, String LajiName,
			String zhonglianging) {
		// 联网获取数据
				// 1.设置参数
				Map<String, Object> jsonMap = new HashMap<String, Object>();

				jsonMap.put("SQYHMC", SQYHMC);
				jsonMap.put("LajiName", LajiName);
				jsonMap.put("zhonglianging", zhonglianging);
				// 界面收集
				// 数据库获取+配置文件
				// 2.访问网络
				HttpClientUtil util = new HttpClientUtil();
				String json = util.sendPost(ConstantValue.LOTTERY_URI
						.concat("PersonalCenter/addLajitoufang"), jsonMap);
				if(json.equals("网络连接不稳定")){
					return "网络连接不稳定";
				}
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

}
