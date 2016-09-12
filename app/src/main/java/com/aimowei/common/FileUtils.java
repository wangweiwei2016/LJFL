package com.aimowei.common;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	private String SDPATH;

	public String getSDPATH() {

		return SDPATH;
	}

	public FileUtils() {
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 
	 * 在SD卡上创建文件
	 */
	public File createSDFile(String FileName) throws IOException {
		File file = new File(SDPATH + FileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 
	 * 在SD卡上创建目录
	 */
	public File createSDDir(String DirName) {
		File dir = new File(SDPATH + DirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 
	 * 判断文件是否存在
	 */
	public boolean isFileExist(String fileName) {

		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 
	 * 将一个文件写如SD?
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}// end write2SDFromInput

}// end FileUtils
