package com.aimowei.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloader {
	private URL url = null;

	/**
	 * 
	 * 根据Url下载文件，前提是，文件时文本形式?
	 * 
	 */
	public String download(String urlString) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			url = new URL(urlString);// 创建url
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();// 创建http连接
			buffer = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));

			while ((line = buffer.readLine()) != null) {

				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}// end download

	/**
	 * 该函数返回整数?1：代表文件出错，0：代表下载成功，1：代表文件已存在
	 */
	public int downFile(String urlString, String path, String fileName) {
		InputStream inputStream = null;
		try {
			FileUtils fileUtils = new FileUtils();
			if (fileUtils.isFileExist(path + fileName)) {
				return 1;
			} else {
				inputStream = getInputStreamFromUrl(urlString);
				File resultFile = fileUtils.write2SDFromInput(path, fileName,
						inputStream);
				if (resultFile == null) {
					return -1;
				}// end if
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}// end finally
		return 0;
	}// end downFile

	/**
	 * 根据URL得到输入?
	 * 
	 */
	public InputStream getInputStreamFromUrl(String urlString)
			throws MalformedURLException, IOException {
		url = new URL(urlString);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();// 创建http连接
		InputStream inputStream = urlConn.getInputStream();

		return inputStream;
	}

}
