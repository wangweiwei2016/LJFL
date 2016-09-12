package com.aimowei.common;

public interface Config {
	int TAKE_PICTURE = 0x000001;
	int CHOOSE_PICTURE_REQUESTCODE = 0x000002;
	int CHOOSE_PICTURE_RESULTCODE = 0x000004;
	/**
	 * 去上传文件
	 */
	int TO_UPLOAD_FILE = 1;
	/**
	 * 上传文件响应
	 */
	int UPLOAD_FILE_DONE = 2;
	/**
	 * 选择文件
	 */
	int TO_SELECT_PHOTO = 3;
	/**
	 * 上传初始化
	 */
	int UPLOAD_INIT_PROCESS = 4;
	/**
	 * 上传中
	 */
	int UPLOAD_IN_PROCESS = 5;
}
