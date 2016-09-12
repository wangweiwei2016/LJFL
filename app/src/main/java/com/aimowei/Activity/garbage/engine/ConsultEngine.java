package com.aimowei.Activity.garbage.engine;



public interface ConsultEngine extends QueryShareEngine{
	/** Action 获取建议咨询类别的URL */
	public static final String QUERY_ACTION1_URL = "Setting/GetAdviceConsultationType";
	/**
	 * @param JYZXLX 建议or咨询 "0" 建议 "1"咨询
	 * @param type  建议咨询类别
	 * @param faqineirong 发起内容
	 * @param faqiren 发起人
	 * @return 
	 */
	public String getConsult(String JYZXLX,String type, String faqineirong, String faqiren) ;
	/** Action 获取建议咨询类别 */
	public String[] getConsultType() ;
}
