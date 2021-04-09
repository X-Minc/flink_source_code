package com.ifugle.rap.service;

public interface DataSyncService {
	/**
	 * @auther: Liuzhengyang
	 * 数据实时同步
	 */
	 void dataSyncInsertIncrementData();

	/***
	 * 初始化本地文件
	 */
	void initLocalTime();

}
