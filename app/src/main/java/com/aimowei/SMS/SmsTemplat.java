package com.aimowei.SMS;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class SmsTemplat {
	public static String version = "2014-06-30";
	public static String server="api.ucpaas.com";
	public static String accountSid="171710c9f6591418b29ab6054ab127ee";
	public static String authToken="bab4db472f7ed9fd69fa358e842282f3";
	public static String appId="e9e27e3e347344fa9cc738cab5904140";
	public static String templateId="7974";
	public static String to="";
	public static String para="";

	public static String getSignature(String accountSid, String authToken,String timestamp,EncryptUtil encryptUtil) throws Exception{
		String sig = accountSid + authToken + timestamp;
		String signature = encryptUtil.md5Digest(sig);
		return signature;
	}
	
	public static HttpResponse get(String cType,String accountSid,String authToken,String timestamp,String url,DefaultHttpClient httpclient,EncryptUtil encryptUtil) throws Exception{
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Accept", cType);//
		httpget.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httpget.setHeader("Authorization",auth);
		HttpResponse response = httpclient.execute(httpget);
		return response;
	}
	
	public static HttpResponse post(String cType,String accountSid,String authToken,String timestamp,String url,DefaultHttpClient httpclient,EncryptUtil encryptUtil,String body) throws Exception{
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Accept", cType);
		httppost.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		BasicHttpEntity requestBody = new BasicHttpEntity();
        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
        requestBody.setContentLength(body.getBytes("UTF-8").length);
        httppost.setEntity(requestBody);
        //鏌ョ湅杩斿洖鍊�
		HttpResponse response = httpclient.execute(httppost);
		return response;
	}
	
	//杞崲鏃堕棿鏍煎紡
	public static String dateToStr(Date date,String pattern) {
	       if (date == null || date.equals(""))
	    	 return null;
	       SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	       return formatter.format(date);
 } 
	
	public static String templateSMS(String accountSid, String authToken,
			String appId, String templateId, String to, String param) {
		// TODO Auto-generated method stub
		String result = "";
		DefaultHttpClient httpclient=new DefaultHttpClient();
		try {
			//MD5鍔犲瘑
			EncryptUtil encryptUtil = new EncryptUtil();
			//鑾峰彇鏃堕棿鎴�
			String timestamp = dateToStr(new Date(), "yyyyMMddHHmmss");//鑾峰彇鏃堕棿鎴�
			String signature =getSignature(accountSid,authToken,timestamp,encryptUtil);
			StringBuffer sb = new StringBuffer("https://");
			sb.append(server);
			String url = sb.append("/").append(version)
					.append("/Accounts/").append(accountSid)
					.append("/Messages/templateSMS")
					.append("?sig=").append(signature).toString();
			TemplateSMS templateSMS=new TemplateSMS();
			templateSMS.setAppId(appId);
			templateSMS.setTemplateId(templateId);
			templateSMS.setTo(to);
			templateSMS.setParam(param);
			Gson gson = new Gson();
			String body = gson.toJson(templateSMS);
			body="{\"templateSMS\":"+body+"}";
			System.out.println("post bpdy is: " + body);

			HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
				System.out.println("templateSMS Response content is: " + result);
			}
			// 纭繚HTTP鍝嶅簲鍐呭鍏ㄩ儴琚鍑烘垨鑰呭唴瀹规祦琚叧闂�
			//EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			//鍏抽棴杩炴帴
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * 鐭俊楠岃瘉鐮佺畝鏄撴帴鍙�*/
//	public static String templateSimpleSMS(String accountSid, String authToken,
//			String appId, String templateId, String to, String param) {
//		// TODO Auto-generated method stub
//		String result = "";
//		String url = "http://www.ucpaas.com/maap/sms/code";
//		String timestamp = dateToStr(new Date(), "yyyyMMddHHmmssSSS");//鑾峰彇鏃堕棿鎴�
//		DefaultHttpClient httpclient=new DefaultHttpClient();
//		try {
//			//MD5鍔犲瘑
//			EncryptUtil encryptUtil = new EncryptUtil();
//			String signature =getSignature(accountSid,timestamp,authToken,encryptUtil);
//			StringBuffer sb = new StringBuffer(url);
//			url = sb.append("?")
//					.append("&sid=").append(accountSid)
//					.append("&appId=").append(appId)
//					.append("&time=").append(timestamp)
//					.append("&sign=").append(signature.toLowerCase())
//					.append("&to=").append(to)
//					.append("&templateId=").append(templateId)
//					.append("&param=").append(param).toString();
//			System.out.println("templateSimpleSMS url = "+url);
//			HttpGet httpget = new HttpGet(url);
//
//			HttpResponse response = httpclient.execute(httpget);
//			HttpEntity entity = response.getEntity();
//			if (entity != null) {
//				result = EntityUtils.toString(entity, "UTF-8");
//				System.out.println("templateSimpleSMS Response content is: " + result);
//			}
//			EntityUtils.consume(entity);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally{
//			// 鍏抽棴杩炴帴
//		    httpclient.getConnectionManager().shutdown();
//		}
//		return result;
//	}

}
