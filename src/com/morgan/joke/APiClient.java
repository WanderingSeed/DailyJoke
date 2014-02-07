package com.morgan.joke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class APiClient {

    private static final String UTF_8 = "UTF-8";
    private final static int TIMEOUT_CONNECTION = 20000;
    private final static int TIMEOUT_SOCKET = 3 * 1000;

    private final static String SERVER_CONNECT_ERROR = "服务器连接失败";

    private final static int RETRY_TIME = 5;

    private static HttpClient getHttpClient()
    {
        HttpClient httpClient = new HttpClient();
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        // 设置 连接超时时间
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
        // 设置 读数据超时时间
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
        // 设置 字符集
        httpClient.getParams().setContentCharset(UTF_8);
        return httpClient;
    }

    private static GetMethod getHttpGet(String url)
    {
        GetMethod httpGet = new GetMethod(url);
        httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
        httpGet.setRequestHeader("Connection", "Keep-Alive");
        return httpGet;
    }

    private static String get(String url)
    {
        HttpClient httpClient = null;
        GetMethod httpGet = null;

        String responseBody = "";
        int time = 0;
        do {
            try {
                httpClient = getHttpClient();
                httpGet = getHttpGet(url);
                int statusCode = httpClient.executeMethod(httpGet);
                if (statusCode != HttpStatus.SC_OK) {
                    responseBody = SERVER_CONNECT_ERROR;
                    break;
                }
                responseBody = httpGet.getResponseBodyAsString();
                break;
            } catch (HttpException e) {
                Log.e("joke","从服务器获取失败", e);
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生致命的异常，可能是协议不对或者返回的内容有问题
                responseBody = SERVER_CONNECT_ERROR;
            } catch (IOException e) {
                Log.e("joke","从服务器获取失败", e);
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生网络异常
                responseBody = SERVER_CONNECT_ERROR;
            } catch (Exception e) {
                Log.e("joke","从服务器获取失败", e);
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                responseBody = SERVER_CONNECT_ERROR;
            } finally {
                // 释放连接
                if (null != httpGet) {
                    httpGet.releaseConnection();
                }
                httpClient = null;
            }
        } while (time < RETRY_TIME);
        return responseBody;
    }

    public List<String> getJokeList()
    {
        Log.e("joke", "开始获取笑话");
        String response = get(Constant.RESOURCEURL);
        Log.e("joke", "系统返回结果： " + response);
        List<String> jokes = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject item = itemsArray.getJSONObject(i);
                String imageUrl = item.getJSONObject("img").getString("img_file");
                //暂时不支持有图片的笑话
                if (null == imageUrl || "".equals(imageUrl.trim()) && Constant.MAX_JOKE_COUNT > jokes.size()) {
                    String joke = item.getString("content");
                    jokes.add(joke);
                }
            }
        } catch (JSONException e) {
        }
        return jokes;
    }
}
