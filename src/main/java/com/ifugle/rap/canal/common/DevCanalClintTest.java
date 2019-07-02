package com.ifugle.rap.canal.common;

import java.net.InetSocketAddress;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.ifugle.rap.canal.service.AbstractCanalClientService;

/**
 * @author LiuZhengyang
 * @version $Id$
 * @since 2018年10月12日 10:24
 */
public class DevCanalClintTest {

    public static void main(String[] args) {
        String destination = "example-1";
        String ip = "localhost";
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(ip, 11111),
        destination,
        "",
        "");
        final AbstractCanalClientService service = new AbstractCanalClientService(destination,connector);
        service.start();
    }


}
