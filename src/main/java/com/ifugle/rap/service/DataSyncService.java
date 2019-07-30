package com.ifugle.rap.service;

public interface DataSyncService {

	/**
	 * @auther: Liuzhengyang
	 * 数据全量同步
	 */
	 void dataSyncInit();

	/**
	 * @auther: Liuzhengyang
	 * 数据实时同步
	 */
	 void dataSyncInsertIncrementData();

	/**
	 * @auther: Liuzhengyang
	 * 数据修改同步
	 */
	 void dataUpdateSync();

	/***
	 * 初始化本地文件
	 */
	void initLocalTime();

}
