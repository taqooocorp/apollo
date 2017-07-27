package com.taqooo.crawler.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 16/11/2                          <br/>
 * Time    : 上午11:15                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
public class HttpClientUtils {
    /**
     * map转字符串
     *
     * @param map
     * @return
     */
    public static String formatMap(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        StringBuffer buff = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            buff.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        //删除最后一个&
        if (buff.length() > 0) {
            buff.deleteCharAt(buff.length() - 1);
        }
        return buff.toString();
    }

    /**
     * 无参数的URL地址
     *
     * @param url
     * @return
     */
    public static String parsetUrlHead(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        int endIndex = url.indexOf('?');
        return endIndex < 0 ? url : url.substring(0, endIndex);
    }

    /**
     * 解析url中的参数部分
     *
     * @param url
     * @return
     */
    public static Map<String, String> parseUrlParams(String url) {
        Map<String, String> paramMap = new HashMap<>();
        if (url == null || url.isEmpty()) {
            return paramMap;
        }
        String paramUrl = url.substring(url.indexOf('?') + 1);
        if (paramUrl == null || paramUrl.isEmpty()) {
            return paramMap;
        }
        String params[] = paramUrl.split("&");
        if (params == null || params.length <= 0) {
            return paramMap;
        }
        for (String param : params) {
            if (param != null || !param.isEmpty()) {
                String keyValue[] = param.split("=");
                if (keyValue != null && keyValue.length == 2) {
                    paramMap.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return paramMap;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url     发送请求的URL
     * @param params  请求参数，请求参数应该是 name1=value1&name2=value2 的形式
     * @param cookies
     * @return 远程资源的响应结果
     */
    public static String sendGet(String url, String params, String cookies) {
        try {
            if (params != null && !params.isEmpty()) {
                url += "?" + params;
            }

            //发送get请求
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");

            //必须设置false，否则会自动redirect到重定向后的地址
            conn.setInstanceFollowRedirects(false);
            if (cookies != null) {
                conn.setRequestProperty("Cookie", cookies);
            }
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            conn.connect();

            //判定是否会进行302重定向
            if (conn.getResponseCode() == 302) {
                //如果会重定向，保存302重定向地址，以及Cookies,然后重新发送请求(模拟请求)
                String location = conn.getHeaderField("Location");
                cookies = conn.getHeaderField("Set-Cookie");

                serverUrl = new URL(location);
                conn = (HttpURLConnection) serverUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Cookie", cookies);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.addRequestProperty("Accept-Charset", "UTF-8;");
                conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
                conn.connect();
                System.out.println("跳转地址:" + location);
            }

            //将返回的输入流转换成字符串
            StringBuffer buffer = new StringBuffer();
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            conn.disconnect();

            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向指定URL发送post方法的请求
     *
     * @param url     发送请求的URL
     * @param body    请求参数，请求参数应该是json的形式
     * @param cookies
     * @return 远程资源的响应结果
     */
    public static String sendPost(String url, JSONObject body, String cookies, String username, String password) {
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);

            if (cookies != null) {
                conn.setRequestProperty("Cookie", cookies);
            }
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8;");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            String authHeader = buildBAHeader(username, password);
            if (authHeader != null) {
                conn.setRequestProperty("Authorization", authHeader);
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10 * 1000);

            conn.connect();

            //json参数
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.write(body.toJSONString().getBytes("utf-8"));
            outputStream.flush();
            outputStream.close();

            //将返回的输入流转换成字符串
            StringBuffer buffer = new StringBuffer();
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            conn.disconnect();

            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构造BA认证头信息
     *
     * @param username
     * @param password
     * @return
     */
    private static String buildBAHeader(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        String auth = "" + username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }
}