package com.aimowei.Activity.garbage.engine.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.engine.UserLoginEngine;
import com.aimowei.Activity.garbage.net.HttpClientUtil;



public class UserLoginEngineImpl implements UserLoginEngine {

	public  String getUserLoginInfoByList(String user,String password) {
		        // 联网获取数据
				// 1.设置参数
				Map<String, Object> jsonMap = new HashMap<String, Object>();

				jsonMap.put("YHZH", user);
				jsonMap.put("YHMM",password);
				// 界面收集
				// 数据库获取+配置文件
				// 2.访问网络
				HttpClientUtil util = new HttpClientUtil();
				String json = util.sendPost(ConstantValue.LOTTERY_URI.concat("Account/Login"), jsonMap);
				// 3数据处理
				// 获取处理结果(状态+数据)
				// 将服务器传回来的数据进行解析--转换成类是Map的格式
			
					return json;
				
				// 4数据持久化
				
			
	
	}

	
}
