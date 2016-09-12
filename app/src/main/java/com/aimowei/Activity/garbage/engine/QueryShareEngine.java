package com.aimowei.Activity.garbage.engine;

/**
 * 查询共享引擎,存放查询用常用常量和UI枚举常量
 * @author SWZ
 */
public interface QueryShareEngine {
	/**查询关键字 - 成功状态*/
	public static final int Status_Succeed= 1;
	/**查询关键字 - 失败状态*/
	public static final int Status_Fail= 0;
	/**查询关键字 - 结果为空*/
	public static final int Status_DataEmpty= 2;
	/**查询关键字 - 状态*/
	public static final String KeyWord_Status= "Status";
	/**查询关键字 - 信息*/
	public static final String KeyWord_Info= "Info";
	/**查询关键字 - 数据*/
	public static final String KeyWord_Datas= "Datas";
	/**错误提示*/
	public static final String Tip_NoReturn= "网络错误,服务器无响应";
	/**错误提示*/
	public static final String Tip_LostStatus= "网络错误,丢失状态码";
	/**错误提示*/
	public static final String Tip_UnknownStatus= "网络错误,出现未知状态码";
	/**错误提示*/
	public static final String Tip_LostInfo= "网络错误,丢失错误提示";
	/**错误提示*/
	public static final String Tip_LostDatas= "网络错误,丢失数据";
	/**错误提示*/
	public static final String Tip_DataEmpty= "查无数据";
	/**错误提示*/
	public static final String Tip_DataFormatError= "网络错误,数据缺损";
	/**Controller 活动消息查询控制器URL*/
	public static final String  URL_Controller_Information= "InformationQuery";	
	/** UI模式枚举值*/
	public enum UIMode {
		/** 加载中 */
		UI_Loading,
		/** 只显示社区标签*/
		UI_OnlyCommunity,
		/** 只显示重试按钮 */
		UI_OnlyRetry,
		/** 只显示提示标签*/
		UI_OnlyTip,
		/** 显示提示标签,社区标签*/
		UI_TipCommunity,
		/** 显示重试按钮 ,社区标签*/
		UI_RetryCommunity,
		/** 状态1 */
		UI_1,
		/** 状态2 */
		UI_2,
		/** 状态3 */
		UI_3,
		/** 状态4 */
		UI_4
		
	}
}
