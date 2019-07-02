/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.service.SyncService;

/**
 * @author LiuZhengyang
 * @version $Id$
 * @since 2018年10月16日 17:17
 */
public class BaseInitThread {

	public Integer pageSize;

	protected SyncService syncService;

	public BaseInitThread(Integer pageSize, SyncService syncService) {
		this.pageSize = pageSize;
		this.syncService = syncService;
	}
}
