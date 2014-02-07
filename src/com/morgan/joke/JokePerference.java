package com.morgan.joke;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class JokePerference {

    public static int getNextJokeNum(Context context)
    {
        SharedPreferences pre = context.getSharedPreferences("jokes", Context.MODE_PRIVATE);
        int num = pre.getInt("next_joke_num", -1);
        SharedPreferences.Editor editor = pre.edit();
        int totalnum = pre.getInt("total_joke_num", -1);
        if (num >= totalnum) {
            if (-1 == totalnum) {
                num = -1;
            } else {
                num = 0;
                // 循环了一圈了，该重新获取了（这是获取笑话的另外一种驱动方式）
                // context.sendBroadcast(new Intent(context, GetJokesReceiver.class));
            }
        }
        editor.putInt("next_joke_num", num + 1);
        editor.commit();
        return num;
    }

    public static String getJoke(Context context, int num)
    {
        Log.e("joke", "获取第" + num + "个笑话");
        SharedPreferences pre = context.getSharedPreferences("jokes", Context.MODE_PRIVATE);
        return pre.getString("joke_" + num, "");
    }

    public static void storeJokes(Context context, List<String> jokes)
    {
        Log.e("joke", "存储笑话" + jokes.size() + "个");
        SharedPreferences pre = context.getSharedPreferences("jokes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        for (int i = 0; i < jokes.size(); i++) {
            editor.putString("joke_" + i, jokes.get(i));
        }
        int totalnum = pre.getInt("total_joke_num", -1);
        editor.putInt("total_joke_num", jokes.size());
        editor.putInt("next_joke_num", 0);
        editor.commit();
        // 第一次创建的时候空白3分钟
        if (-1 == totalnum) {
            context.startService(new Intent(context, UpdateJokeService.class));
        }
    }

}
