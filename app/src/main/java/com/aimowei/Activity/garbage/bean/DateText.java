package com.aimowei.Activity.garbage.bean;

public class DateText {
	/**
	 * 级别名称
	 * 
	 */
	private String date;
	/**
	 * 保留 暂时没用到
	 * 
	 */
	private String text;
	/**
	 * 级别对应的数字
	 * 
	 */
	private String number;
	/**
	 * 隐藏图标
	 * 
	 */
	private Boolean yincangtubiao;
	/**树干是否变色
	 * 
	 */
	private Boolean isGET;
	/**用户标志在哪
	 * 
	 */
	private Boolean isIn;

	public DateText() {

	}

	public DateText(String date, String text, String number,
			Boolean yincangtubiao, Boolean isGET, Boolean isIn) {
		super();
		this.date = date;
		this.text = text;
		this.number = number;
		this.yincangtubiao = yincangtubiao;
		this.isGET = isGET;
		this.isIn = isIn;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Boolean getIsIn() {
		return isIn;
	}

	public void setIsIn(Boolean isIn) {
		this.isIn = isIn;
	}

	public Boolean getYincangtubiao() {
		return yincangtubiao;
	}

	public void setYincangtubiao(Boolean yincangtubiao) {
		this.yincangtubiao = yincangtubiao;
	}

	public Boolean getIsGET() {
		return isGET;
	}

	public void setIsGET(Boolean isGET) {
		this.isGET = isGET;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
