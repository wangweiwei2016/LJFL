package com.aimowei.UpLoad;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import android.util.Log;

public class UploadUtil {
	private static UploadUtil uploadUtil;
	private static final String BOUNDARY =  UUID.randomUUID().toString(); // 锟竭斤拷锟绞� 锟斤拷锟斤拷锟斤拷锟�
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 锟斤拷锟斤拷锟斤拷锟斤拷
	private UploadUtil() {

	}

	/**
	 * 锟斤拷锟斤拷模式锟斤拷取锟较达拷锟斤拷锟斤拷锟斤拷
	 * @return
	 */
	public static UploadUtil getInstance() {
		if (null == uploadUtil) {
			uploadUtil = new UploadUtil();
		}
		return uploadUtil;
	}

	private static final String TAG = "UploadUtil";
	private int readTimeOut = 10 * 1000; // 锟斤拷取锟斤拷时
	private int connectTimeout = 10 * 1000; // 锟斤拷时时锟斤拷
	/***
	 * 锟斤拷锟斤拷使锟矫多长时锟斤拷
	 */
	private static int requestTime = 0;
	
	private static final String CHARSET = "utf-8"; // 锟斤拷锟矫憋拷锟斤拷

	/***
	 * 锟较达拷锟缴癸拷
	 */
	public static final int UPLOAD_SUCCESS_CODE = 1;
	/**
	 * 锟侥硷拷锟斤拷锟斤拷锟斤拷
	 */
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	public static final int UPLOAD_SERVER_ERROR_CODE = 3;
	protected static final int WHAT_TO_UPLOAD = 1;
	protected static final int WHAT_UPLOAD_DONE = 2;
	
	/**
	 * android锟较达拷锟侥硷拷锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param filePath
	 *            锟斤拷要锟较达拷锟斤拷锟侥硷拷锟斤拷路锟斤拷
	 * @param fileKey
	 *            锟斤拷锟斤拷页锟斤拷<input type=file name=xxx/> xxx锟斤拷锟斤拷锟斤拷锟斤拷锟絝ileKey
	 * @param RequestURL
	 *            锟斤拷锟斤拷锟経RL
	 */
	public void uploadFile(String filePath, String fileKey, String RequestURL,
			Map<String, String> param) {
		if (filePath == null) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
			return;
		}
		try {
			File file = new File(filePath);
			uploadFile(file, fileKey, RequestURL, param);
		} catch (Exception e) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"上传错误");
			e.printStackTrace();
			return;
		}
	}

	/**
	 * android锟较达拷锟侥硷拷锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param file
	 *            锟斤拷要锟较达拷锟斤拷锟侥硷拷
	 * @param fileKey
	 *            锟斤拷锟斤拷页锟斤拷<input type=file name=xxx/> xxx锟斤拷锟斤拷锟斤拷锟斤拷锟絝ileKey
	 * @param RequestURL
	 *            锟斤拷锟斤拷锟経RL
	 */
	public void uploadFile(final File file, final String fileKey,
			final String RequestURL, final Map<String, String> param) {
		if (file == null || (!file.exists())) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
			return;
		}

		Log.i(TAG, "锟斤拷锟斤拷锟経RL=" + RequestURL);
		Log.i(TAG, "锟斤拷锟斤拷锟絝ileName=" + file.getName());
		Log.i(TAG, "锟斤拷锟斤拷锟絝ileKey=" + fileKey);
		new Thread(new Runnable() {  //锟斤拷锟斤拷锟竭筹拷锟较达拷锟侥硷拷
			@Override
			public void run() {
				toUploadFile(file, fileKey, RequestURL, param);
			}
		}).start();
		
	}

	private void toUploadFile(File file, String fileKey, String RequestURL,
			Map<String, String> param) {
		String result = null;
		requestTime= 0;
		
		long requestTime = System.currentTimeMillis();
		long responseTime = 0;

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeOut);
			conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
			conn.setDoOutput(true); // 锟斤拷锟斤拷锟斤拷锟斤拷锟�
			conn.setUseCaches(false); // 锟斤拷锟斤拷锟斤拷使锟矫伙拷锟斤拷
			conn.setRequestMethod("POST"); // 锟斤拷锟斤拷式
			conn.setRequestProperty("Charset", CHARSET); // 锟斤拷锟矫憋拷锟斤拷
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			/**
			 * 锟斤拷锟侥硷拷锟斤拷为锟秸ｏ拷锟斤拷锟侥硷拷锟斤拷装锟斤拷锟斤拷锟较达拷
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = "";
			
			/***
			 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟较达拷锟斤拷锟斤拷
			 */
			if (param != null && param.size() > 0) {
				Iterator<String> it = param.keySet().iterator();
				while (it.hasNext()) {
					sb = null;
					sb = new StringBuffer();
					String key = it.next();
					String value = param.get(key);
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
					sb.append(value).append(LINE_END);
					params = sb.toString();
					Log.i(TAG, key+"="+params+"##");
					dos.write(params.getBytes());
//					dos.flush();
				}
			}
			
			sb = null;
			params = null;
			sb = new StringBuffer();
			/**
			 * 锟斤拷锟斤拷锟截碉拷注锟解： name锟斤拷锟斤拷锟街滴拷锟斤拷锟斤拷锟斤拷锟斤拷锟揭猭ey 只锟斤拷锟斤拷锟絢ey 锟脚匡拷锟皆得碉拷锟斤拷应锟斤拷锟侥硷拷
			 * filename锟斤拷锟侥硷拷锟斤拷锟斤拷锟街ｏ拷锟斤拷锟斤拷锟斤拷缀锟斤拷锟斤拷 锟斤拷锟斤拷:abc.png
			 */
			sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
			sb.append("Content-Disposition:form-data; name=\"" + fileKey
					+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
			sb.append("Content-Type:image/pjpeg" + LINE_END); // 锟斤拷锟斤拷锟斤拷锟矫碉拷Content-type锟斤拷锟斤拷要锟斤拷 锟斤拷锟斤拷锟节凤拷锟斤拷锟斤拷锟剿憋拷锟斤拷募锟斤拷锟斤拷锟斤拷偷锟�
			sb.append(LINE_END);
			params = sb.toString();
			sb = null;
			
			Log.i(TAG, file.getName()+"=" + params+"##");
			dos.write(params.getBytes());
			/**锟较达拷锟侥硷拷*/
			InputStream is = new FileInputStream(file);
			onUploadProcessListener.initUpload((int)file.length());
			byte[] bytes = new byte[1024];
			int len = 0;
			int curLen = 0;
			while ((len = is.read(bytes)) != -1) {
				curLen += len;
				dos.write(bytes, 0, len);
				onUploadProcessListener.onUploadProcess(curLen);
			}
			is.close();
			
			dos.write(LINE_END.getBytes());
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
			dos.write(end_data);
			dos.flush();
//			
//			dos.write(tempOutputStream.toByteArray());
			/**
			 * 锟斤拷取锟斤拷应锟斤拷 200=锟缴癸拷 锟斤拷锟斤拷应锟缴癸拷锟斤拷锟斤拷取锟斤拷应锟斤拷锟斤拷
			 */
			int res = conn.getResponseCode();
			responseTime = System.currentTimeMillis();
			UploadUtil.requestTime = (int) ((responseTime-requestTime)/1000);
			Log.e(TAG, "response code:" + res);
			if (res == 200) {
				Log.e(TAG, "request success");
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				Log.e(TAG, "result : " + result);
				sendMessage(UPLOAD_SUCCESS_CODE, ""
						+ result);
				return;
			} else {
				Log.e(TAG, "request error");
				sendMessage(UPLOAD_SERVER_ERROR_CODE,"code=" + res);
				return;
			}
		} catch (MalformedURLException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE,"锟较达拷失锟杰ｏ拷error=" + e.getMessage());
			e.printStackTrace();
			return;
		} catch (IOException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE,"锟较达拷失锟杰ｏ拷error=" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 锟斤拷锟斤拷锟较达拷锟斤拷锟�
	 * @param responseCode
	 * @param responseMessage
	 */
	private void sendMessage(int responseCode,String responseMessage)
	{
		onUploadProcessListener.onUploadDone(responseCode, responseMessage);
	}
	
	/**
	 * 锟斤拷锟斤拷锟斤拷一锟斤拷锟皆讹拷锟斤拷幕氐锟斤拷锟斤拷锟斤拷锟斤拷玫锟斤拷氐锟斤拷洗锟斤拷募锟斤拷欠锟斤拷锟斤拷
	 * 
	 * @author shimingzheng
	 * 
	 */
	public static interface OnUploadProcessListener {
		/**
		 * 锟较达拷锟斤拷应
		 * @param responseCode
		 * @param message
		 */
		void onUploadDone(int responseCode, String message);
		/**
		 * 锟较达拷锟斤拷
		 * @param uploadSize
		 */
		void onUploadProcess(int uploadSize);
		/**
		 * 准锟斤拷锟较达拷
		 * @param fileSize
		 */
		void initUpload(int fileSize);
	}
	private OnUploadProcessListener onUploadProcessListener;
	
	

	public void setOnUploadProcessListener(
			OnUploadProcessListener onUploadProcessListener) {
		this.onUploadProcessListener = onUploadProcessListener;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	/**
	 * 锟斤拷取锟较达拷使锟矫碉拷时锟斤拷
	 * @return
	 */
	public static int getRequestTime() {
		return requestTime;
	}
	
	public static interface uploadProcessListener{
		
	}
	
	
	
	
}
