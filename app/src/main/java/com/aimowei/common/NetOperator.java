package com.aimowei.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

public class NetOperator {
	private HttpResponse httpResponse = null;
	private HttpEntity httpEntity = null;

	public String PostRequest(String baseUrl, List<NameValuePair> namePairValus) {
		String jsonData="";
		try {
			try {
				HttpPost httpPost = new HttpPost(baseUrl);
				/*	httpPost.addHeader("Content-Type", "application/json");  */     
				httpPost.addHeader("charset", HTTP.UTF_8); 
				HttpEntity requestHttpEntity = new UrlEncodedFormEntity(namePairValus,HTTP.UTF_8);
				httpPost.setEntity(requestHttpEntity);
				HttpClient httpClient = new DefaultHttpClient();//parms
				InputStream inputStream = null;
				try {
					httpResponse = httpClient.execute(httpPost);
					httpEntity = httpResponse.getEntity();
					inputStream = httpEntity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream));
					String result = "";
					String line = "";
					while ((line = reader.readLine()) != null) {
						result = result + line;
					}
					jsonData = result;
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						inputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}// END Try
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// End Catch

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonData;
	}

	public String GetResquest(String baseUrl) {
		String jsonData = "";
		if (baseUrl.isEmpty())
			return "0";
		HttpGet httpGet = new HttpGet(baseUrl);
		httpGet.addHeader("Content-Type", "application/json");       
		httpGet.addHeader("charset", HTTP.UTF_8); 
		HttpParams parms = new BasicHttpParams();
		parms.setParameter("charset", HTTP.UTF_8);
		HttpConnectionParams.setConnectionTimeout(parms, 8 * 1000);
		HttpConnectionParams.setSoTimeout(parms, 8 * 1000);
		HttpClient httpClient = new DefaultHttpClient(parms);
		InputStream inputStream = null;
		try {
			httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while ((line = reader.readLine()) != null) {
				result = result + line;
			}
			jsonData = result;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}// END Http
		return jsonData;
	}

}
