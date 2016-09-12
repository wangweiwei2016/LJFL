package com.aimowei.Activity.garbage;

import java.util.ArrayList;
import java.util.List;

import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class GlobalParams extends Application{
	/**
	 * 代理的ip
	 */
	public static String PROXY_IP="";
	/**
	 * 代理的端口
	 */
	public static int PORT=0;
	/**
	 * 屏幕的宽度
	 */
	public static Context CONTEXT;
	/**
	 * 这个可能为用户帐号，可能是手机
	 */
	public static String User="";
	/**
	 * 是否登录
	 */
	public static boolean isLogin=false;
	static List<Activity> activitys=new ArrayList<Activity>();

	/**
	 * 用户类别 登录传过来的 "0"为管理员，”1“为普通用户，”2“为外来志愿者
	 */
	public static String yonghuleibie="";
	/**
	 * 用户社区名称 登录传过来的 社区用户才有的
	 */
	public static String SQYHMC="";
	/**
	 * 邮箱
	 */
	public static String YX="";
	/**
	 * 社区代码，普通用户是中文名称，管理员是社区代码
	 */
	public static String SQDM="";
	/**
	 * 移动电话
	 */
	public static String YDDH="";
	/**
	 * 用户帐号，注意这个不是登录帐号是 特指用户帐号
	 */
	public static String YHZH="";
	/**
	 * 社区用户才有户主姓名
	 */
	public static String HZXM="";
	
}
