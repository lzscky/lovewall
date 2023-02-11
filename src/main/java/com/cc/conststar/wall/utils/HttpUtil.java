package com.cc.conststar.wall.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class HttpUtil {

    /**
     * 向指定URL发送POST方法的请求 Content-Type=application/x-www-form-urlencoded
     *
     * @param targetUrl 发送请求的URL
     * @param params    请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return JSONObject 返回的JSON数据
     */
    public static JSONObject postFormUrlEncoded(String targetUrl, String params) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(targetUrl.trim());
            urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求方式
            urlConnection.setRequestMethod("POST");
            // 设置数据类型
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置允许输入输出
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // 设置不用缓存
            urlConnection.setUseCaches(false);

            urlConnection.connect();
            PrintWriter out = new PrintWriter(new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8));
            // 写入传递参数,格式为a=b&c=d
            out.print(params);
            out.flush();

            int resultCode = urlConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                StringBuffer stringBuffer = new StringBuffer();
                String readLine;
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
                while ((readLine = responseReader.readLine()) != null) {
                    stringBuffer.append(readLine);
                }
                responseReader.close();
                return JSONObject.parseObject(stringBuffer.toString());
            }
            out.close();
        } catch (Exception e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public   static JSONObject doGet (String serverUrl) {
        // 用JAVA发起https请求，并返回json格式的结果

        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(500);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return JSONObject.parseObject(result.toString());
    }



}
