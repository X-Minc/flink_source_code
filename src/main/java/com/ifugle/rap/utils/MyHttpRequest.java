/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;

/**
 * @author HuangLei(wenyuan)
 * @version $Id MyHttpRequest.java v 0.1 2019/1/4 HuangLei(wenyuan) Exp $
 */
public class MyHttpRequest {

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *         发送请求的URL
     * @param map
     *         请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @param charset
     *         发送和接收的格式
     *
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, Object> map, String charset) {
        StringBuffer sb = new StringBuffer();
        //构建请求参数
        if (map != null && map.size() > 0) {
            Iterator it = map.entrySet().iterator(); //定义迭代器
            while (it.hasNext()) {
                Map.Entry er = (Map.Entry) it.next();
                sb.append(er.getKey());
                sb.append("=");
                sb.append(er.getValue());
                sb.append("&");
            }
        }
        return sendGet(url, sb.toString(), charset);
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url
     *         发送请求的URL
     * @param map
     *         请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @param charset
     *         发送和接收的格式
     *
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, Object> map, String charset) {
        StringBuffer sb = new StringBuffer();
        //构建请求参数
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> e : map.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
        }
        return sendPost(url, sb.toString(), charset);
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *         发送请求的URL
     * @param param
     *         请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param charset
     *         发送和接收的格式
     *
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGetSSL(String url, String param, String charset) {
        String result = "";
        String line;
        StringBuffer sb = new StringBuffer();
        BufferedReader in = null;
        try {
            String urlNameString ="";
            if(StringUtils.isBlank(param)){
                urlNameString = url;
            }else{
                urlNameString = url + "?" + param;
            }
            URL realUrl = new URL(urlNameString);

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();


            // 打开和URL之间的连接
            HttpsURLConnection conn =  (HttpsURLConnection) realUrl.openConnection();
            // 设置通用的请求属性 设置请求格式
            conn.setRequestProperty("contentType", charset);
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            //设置超时时间
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            // 建立实际的连接
            conn.connect();
            // 定义 BufferedReader输入流来读取URL的响应,设置接收格式
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    public static String sendGet(String url, String param, String charset) {
        String result = "";
        String line;
        StringBuffer sb = new StringBuffer();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);

            // 打开和URL之间的连接
            HttpURLConnection conn =  (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性 设置请求格式
            conn.setRequestProperty("contentType", charset);
            //设置超时时间
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            // 建立实际的连接
            conn.connect();
            // 定义 BufferedReader输入流来读取URL的响应,设置接收格式
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *         发送请求的 URL
     * @param param
     *         请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param charset
     *         发送和接收的格式
     *
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, String charset) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String line;
        StringBuffer sb = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性 设置请求格式
            conn.setRequestProperty("contentType", charset);
            //设置超时时间
            conn.setConnectTimeout(60);
            conn.setReadTimeout(60);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (Exception e) {
            System.out.println("发送 POST请求出现异常!" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

        public static void main(String[] args) {
            String getUrl="https://zhcs-test-cdn.dingtax.cn/test/id4a34cd6184ee4abf859f70a2e991abbd.HTML?Expires=1859800718&OSSAccessKeyId=LTAIfbtzzdt9Cv6e&Signature=EPvsS%2BQmxFKsoKnLs8d9CfdTjMo%3D";
            String param="";
            System.out.println("Get请求1:"+MyHttpRequest.sendGetSSL(getUrl, param,"utf-8"));
        }
}
