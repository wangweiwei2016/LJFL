package com.aimowei.Activity.garbage.engine.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.engine.EventArrangementEngine;
import com.aimowei.Activity.garbage.information_function.EventArrangementActivity;
import com.aimowei.Activity.garbage.net.HttpClientUtil;
import com.aimowei.garbage.R;

/**
 * 活动查询接口实现类
 * 
 * @author SWZ
 */
public class EventArrangementEngineImpl implements EventArrangementEngine {
	/** 错误信息 */
	public static String errorMsg = null;
	/** 是否成功,成功就不取错误信息分析了 */
	public static boolean isSucceed = false;

	@Override
	public LinkedList<EventArrangementModel> queryActivityList(String SQDM,
			int nextIndex, int maxlength) {

		HttpClientUtil util = new HttpClientUtil();
		// 拼凑URL
		String url = ConstantValue.LOTTERY_URI.concat(
				URL_Controller_Information).concat(QUERY_ACTION4_URL);
		// 填充参数
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Value1", SQDM);
		jsonMap.put("Value2", String.valueOf(nextIndex));
		jsonMap.put("Value3", String.valueOf(maxlength));
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
			switch (status) {// 判断状态
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
			LinkedList<EventArrangementModel> activitys = new LinkedList<EventArrangementModel>();
			EventArrangementModel tmpModel = null;
			for (int i = 0; i < length; i++) {//数据填充
				dataObj = datas.getJSONObject(i);
				tmpModel = new EventArrangementModel();
				tmpModel.id = dataObj.getString("ID");
				tmpModel.startTime = dataObj.getString("StartTime");
				tmpModel.endTime = dataObj.getString("EndTime");
				tmpModel.type = dataObj.getString("Type");
				tmpModel.title = dataObj.getString("Name");
				activitys.addLast(tmpModel);
			}
			isSucceed = true;
			return activitys;

		} catch (Exception e) {
			errorMsg = Tip_DataFormatError;
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String queryActivityDetail(String activityID) {

		HttpClientUtil util = new HttpClientUtil();
		// 填充参数
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Value1", activityID);
		// 拼凑URL
		String url = ConstantValue.LOTTERY_URI.concat(
				URL_Controller_Information).concat(QUERY_ACTION2_URL);
		// 发送请求
		String json = util.sendPost(url, jsonMap);
		isSucceed = false;
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
			if (!topObject.has(KeyWord_Datas)) {// 是否有数据
				errorMsg = Tip_LostDatas;
				return null;
			}
			isSucceed = true;
			return topObject.getString(KeyWord_Datas);
		} catch (Exception e) {
			errorMsg = Tip_DataFormatError;
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 社区标签Adapter
	 */
	private static ArrayAdapter<String> adapter_spinner = null;
	/**
	 * 社区信息Model链表
	 */
	private static LinkedList<CommunityInfoModel> communityInfos = null;
	/**
	 * 设置指定Spinner的选中item,慎用
	 * @param position 选中item的索引
	 * @param spinner Spinner
	 */
	public static void setSelectedSpinnerItem(int position, Spinner spinner) {
		if (spinner.getSelectedItemPosition() != position)
			spinner.setSelection(position, true);
	}

	/**
	 * 使用不知道是社区代码还是社区名称的字符串获取item索引
	 * 
	 * @param SQDM
	 *            输入为null时返回第一个社区代码
	 * @return 返回要设定的初始spinnerPositon -1为不可用
	 */
	public static int getSelectedSpinnerItemPosition(String SQDM) {
		Log.e("SWZ-getSelectedSpinnerItemPosition", "SQDM=" + SQDM);
		if ((SQDM == null || SQDM.isEmpty()))
			if (communityInfos != null && !communityInfos.isEmpty())
				return 0;
		for (CommunityInfoModel model : communityInfos) {
			if (model.name.equals(SQDM))
				return communityInfos.indexOf(model);
			if (model.id.equals(SQDM))
				return communityInfos.indexOf(model);
		}
		if (communityInfos != null && !communityInfos.isEmpty()) {
			// return 0;//默认为0
			for (CommunityInfoModel model : communityInfos) {
				if (model.name.equals("乐海社区"))
					return communityInfos.indexOf(model);
			}
			return 0;
		} else
			return -1;
	}

	/**
	 * 使用通过索引获得社区代码,<0返回默认值
	 * 
	 * @param positon
	 * @return 返回要设定的初始spinnerPositon -1为不可用
	 */
	public static String getSQDM(int positon) {
		if (communityInfos != null && !communityInfos.isEmpty()) {
			if (positon < 0)
				return communityInfos.get(0).id;
			else
				return communityInfos.get(positon).id;
		} else
			return null;
	}

	/**
	 * 对指定的Spinnner更新数据
	 * 
	 * @param spinner
	 *            return true成功,false失败
	 */
	public static boolean updateSpinnerAdapter(Context context, Spinner spinner) {
		if (communityInfos != null && !communityInfos.isEmpty()) {
			if (adapter_spinner == null
					|| !adapter_spinner.equals(spinner.getAdapter())) {
				adapter_spinner = new ArrayAdapter<String>(context,
						R.layout.item_community_spinner, R.id.textView_main);
				for (EventArrangementEngine.CommunityInfoModel model : EventArrangementEngineImpl.communityInfos) {
					adapter_spinner.add(EventArrangementActivity
							.getFitString(model.name));
				}
				// adapter_spinner
				spinner.setAdapter(adapter_spinner);
			}
			return true;
		} else
			return false;
	}

	/**
	 * 联网查询社区信息数据,使用前请检查网络,且不在主线程操作
	 * 
	 * @return true成功,false失败
	 */
	public static boolean updateCommutityInfo(String account) {
		if (communityInfos != null && !communityInfos.isEmpty())
			return true;
		HttpClientUtil util = new HttpClientUtil();
		//拼凑URL
		String url = ConstantValue.LOTTERY_URI.concat(
				URL_Controller_Information).concat(QUERY_ACTION3_URL);
		//填充参数
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("Value1", account);
		//发送请求
		String json = util.sendPost(url, jsonMap);
		isSucceed = false;
		if (json == null || json.isEmpty()) {//判断返回值
			errorMsg = Tip_NoReturn;
			return false;
		}
		// 将服务器传回来的数据进行解析--转换成类是Map的格式
		try {
			JSONObject topObject = new JSONObject(json);
			if (!topObject.has(KeyWord_Status)) {//是否有状态
				errorMsg = Tip_LostStatus;
				return false;
			}
			int status = topObject.getInt(KeyWord_Status);
			if (topObject.has(KeyWord_Info)) {//是否有信息
				errorMsg = topObject.getString(KeyWord_Info);
			} else {
				errorMsg = Tip_LostInfo;
			}
			switch (status) {
			case Status_Fail:
				return false;
			case Status_Succeed:
				break;
			case Status_DataEmpty:
				isSucceed = true;
				return true;
			default:
				errorMsg = Tip_UnknownStatus;
				return false;
			}
			if (!topObject.has(KeyWord_Datas)) {//是否有数据
				errorMsg = Tip_LostDatas;
				return false;
			}
			JSONArray datas = topObject.getJSONArray(KeyWord_Datas);
			int length = datas.length();
			JSONObject dataObj = null;
			if (length <= 0) {//判断是否有数据(极端情况,前面状态说有数据但是这里却没有数据??不大可能发生,发生则为后台出错)
				errorMsg = Tip_DataEmpty;
				return false;
			}
			LinkedList<CommunityInfoModel> infos = new LinkedList<CommunityInfoModel>();
			CommunityInfoModel tmpModel = null;
			for (int i = 0; i < length; i++) {//数据填充
				dataObj = datas.getJSONObject(i);
				tmpModel = new CommunityInfoModel();
				tmpModel.id = dataObj.getString("ID");
				tmpModel.name = dataObj.getString("Name");
				infos.addLast(tmpModel);
			}
			communityInfos = infos;
			isSucceed = true;
			return true;

		} catch (JSONException e) {
			errorMsg = Tip_DataFormatError;
			e.printStackTrace();
			return false;
		}
	}

}
