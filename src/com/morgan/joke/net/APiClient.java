package com.morgan.joke.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.morgan.joke.data.Constant;
import com.morgan.joke.util.Logger;

/**
 * 发送网络请求的客户端
 * 
 * @author Morgan.Ji
 * 
 */
public class APiClient {

    private final static String SERVER_CONNECT_ERROR = "服务器连接失败";

    private final static int RETRY_TIME = 5;

    private static String get(String url) {
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String responseBody = "";
        int time = 0;
        do {
            try {
                httpClient = new DefaultHttpClient();
                httpGet = new HttpGet(url);
                HttpResponse response = httpClient.execute(httpGet);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    responseBody = SERVER_CONNECT_ERROR;
                    break;
                } else {
                    responseBody = EntityUtils.toString(response.getEntity());
                    break;
                }
            } catch (Exception e) {
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
                httpClient = null;
            }
        } while (time < RETRY_TIME);
        return responseBody;
    }

    public List<String> getJokeList(int number) {
        Logger.e("joke", "开始获取笑话");
        // 因为暂时不支持带图片的笑话顾网络请求的笑话个数要多一点
        String response = get(Constant.RESOURCEURL + (number + 10));
        Logger.e("joke", "系统返回结果： " + response);
        List<String> jokes = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject item = itemsArray.getJSONObject(i);
                String imageUrl = item.getJSONObject("img").getString("img_file");
                // 暂时不支持有图片的笑话
                if (null == imageUrl || "".equals(imageUrl.trim())) {
                    String joke = item.getString("content");
                    jokes.add(joke);
                    if (number <= jokes.size()) {
                        break;
                    }
                }
            }
        } catch (JSONException e) {
        }
        return jokes;
    }
}
