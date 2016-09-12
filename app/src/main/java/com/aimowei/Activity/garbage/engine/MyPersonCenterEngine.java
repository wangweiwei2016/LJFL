package com.aimowei.Activity.garbage.engine;

public interface MyPersonCenterEngine {
	
	/**
	 * 修改密码
	 * 
	 * @param user 用户名
	 * @param password 密码
	 * @param newpassword 新密码
	 * @return
	 */
	public String updatePassword(String user, String password,
			String newpassword);// 修改密码

	/**
	 * 获得积分
	 * @param yonghuleibie 用户类别
	 * @param phone 手机号码
	 * @return
	 */
	public String getCredit(String yonghuleibie,String phone);
	/**
	 * 修改用户信息
	 * @param younghuleibie 用户类别 
	 * @param phone 手机号 
	 * @param newphone 新的手机号
	 * @param YX 邮箱
	 * @return
	 */
	public String UpdateUserInfo(String younghuleibie,String phone,String newphone,String YX);
	/**
	 * 添加垃圾重量
	 * @param SQYHMC 社区用户名称
	 * @param LajiName 垃圾类别名 
	 * @param zhonglianging 重量
	 * @return
	 */
	public  String addLajiToufang(String SQYHMC, String LajiName, String zhonglianging);
}
