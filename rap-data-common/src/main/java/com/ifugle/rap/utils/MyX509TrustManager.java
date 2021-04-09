/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.utils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author HuangLei(wenyuan)
 * @version $Id MyX509TrustManager.java v 0.1 2019/1/4 HuangLei(wenyuan) Exp $
 */

public class MyX509TrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    public X509Certificate[] getAcceptedIssuers() {

        return null;
    }

}
