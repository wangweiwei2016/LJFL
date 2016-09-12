package com.aimowei.Activity.garbage.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.net.ParseException;

import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.GlobalParams;


public class HttpClientUtil {
	private HttpClient client;

	private HttpGet get;
	private HttpPost post;

	private HttpResponse response;
//公告头部
	private static Header[] headers;
	static {
		headers = new BasicHeader[10];
		headers[0] = new BasicHeader("Appkey", "12343");
		headers[1] = new BasicHeader("Udid", "");// 手机串号
		headers[2] = new BasicHeader("Os", "android");//
		headers[3] = new BasicHeader("Osversion", "");//
		headers[4] = new BasicHeader("Appversion", "");// 1.0
		headers[5] = new BasicHeader("Sourceid", "");//
		headers[6] = new BasicHeader("Ver", "");

		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");

		headers[9] = new BasicHeader("Unique", "");
	}

	public HttpClientUtil() {
		client= new DefaultHttpClient();
		// 判断是否需要设置代理信息
		if (StringUtils.isNotBlank(GlobalParams.PROXY_IP)) {
			// 设置代理信息
			HttpHost host = new HttpHost(GlobalParams.PROXY_IP, GlobalParams.PORT);
			client.getParams()
					.setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
		}
	}

	/**
	 * 向指定的链接发送xml文件
	 * 
	 * @param uri
	 * @param xml
	 */
	public InputStream sendXml(String uri, String xml) {
		post = new HttpPost(uri);

		try {
			StringEntity entity = new StringEntity(xml, ConstantValue.ENCONDING);
			post.setEntity(entity);

			HttpResponse response = client.execute(post);

			// 200
			if (response.getStatusLine().getStatusCode() == 200) {
				return response.getEntity().getContent();
			}

		} catch (Exception e) {
			e.printStackTrace();
			
		}

		return null;

	}
	/**
	 * 发送get请求
	 */
	public String sendGet(String uri) {

			get = new HttpGet(uri);
			get.setHeaders(headers);
			try {
				response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == 200) {
					return EntityUtils.toString(response.getEntity(), ConstantValue.ENCONDING);
				}else{
					System.out.println(response.getStatusLine().getStatusCode());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "网络连接错误";
			}
		
		return "";
	}
	/**
	 * 发送Post请求
	 * 
	 * @param uri
	 * @param params
	 *            ：参数
	 * @return
	 */
	public String sendPost(String uri, Map<String, Object> params) {
		post = new HttpPost(uri);

		post.setHeaders(headers);

		// 处理超时
		HttpParams httpParams = new BasicHttpParams();//
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams,1000);
		post.setParams(httpParams);

		// 设置参数
		if (params != null && params.size() > 0) {
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Entry<String, Object> item : params.entrySet()) {
				BasicNameValuePair pair = new BasicNameValuePair(item.getKey(),
						(String) item.getValue());
				parameters.add(pair);
			}
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
						parameters, ConstantValue.ENCONDING);
				post.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "网络连接不稳定";
			}
		}

		try {
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(),
						ConstantValue.ENCONDING);
			}else{
				return "网络连接不稳定";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "网络连接不稳定";
		}

		//return "";
	}
	

}
