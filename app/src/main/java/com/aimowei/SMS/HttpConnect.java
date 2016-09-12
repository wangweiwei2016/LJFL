package com.aimowei.SMS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DownloadManager.Query;

public class HttpConnect {

	static TrustManager[] yzxArray = new HttpsTrustManager[] { new HttpsTrustManager() };

	public static JSONObject doGet(String buffer) throws IOException,
			JSONException {
		HttpURLConnection http = null;
		InputStream is = null;
		JSONObject jsonOuter = null;
		String jsonStr = null;
		URL url;
		url = new URL(buffer);
		Query obj = new Query();
		// �ж���http������https����
		if (url.getProtocol().toLowerCase().equals("https")) {
			trustAllHosts();
			http = (HttpsURLConnection) url.openConnection();
			((HttpsURLConnection) http).setHostnameVerifier(DO_NOT_VERIFY);// ������������ȷ��

		} else {
			http = (HttpURLConnection) url.openConnection();
		}
		http.setConnectTimeout(10000);// ���ó�ʱʱ��
		http.setReadTimeout(50000);
		http.setRequestMethod("GET");// ������������Ϊ
		http.setDoInput(true);
		http.setRequestProperty("Content-Type", "text/xml");
		// http.getResponseCode());http��https����״̬200����403
		int httpcode = http.getResponseCode();
		if (httpcode == 200) {
			is = http.getInputStream();
		}
		if (is != null) {
			jsonStr = convertStreamToString(is);
			is.close();
			is = null;
		}
		if (http != null) {
			http.disconnect();
			http = null;
		}
		if (jsonStr != null) {
			jsonOuter = new JSONObject(jsonStr);
		}
		return jsonOuter;

	}

	/**
	 * ������������-�����κ�֤�鶼�������?
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		// Android ����X509��֤����Ϣ����
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, yzxArray, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);//
			// ������������ȷ��
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			// TODO Auto-generated method stub
			// System.out.println("Warning: URL Host: " + hostname + " vs. "
			// + session.getPeerHost());
			return true;
		}
	};

	/**
	 * ��http�Ķ���ֹ��ת����JSON�ַ�
	 * 
	 * @param is
	 * @return
	 * @author: xiaozhenhua
	 * @throws IOException
	 * @data:2014-4-9 ����4:13:06
	 */
	private static synchronized String convertStreamToString(InputStream is)
			throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = is.read(buffer)) != -1) {
			stream.write(buffer, 0, length);
		}
		stream.flush();
		stream.close();
		is.close();
		return stream.toString();
	}
}
