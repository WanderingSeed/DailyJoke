package com.morgan.joke;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * 用于存储和获取存储区的笑话。
 * 
 * @author Morgan.Ji
 * 
 */
public class JokePerference {

    public static int getNextJokeNum(Context context) {
        SharedPreferences pre = context.getSharedPreferences("jokes", Context.MODE_PRIVATE);
        int num = pre.getInt("next_joke_num", 0);
        SharedPreferences.Editor editor = pre.edit();
        int totalnum = pre.getInt("total_joke_num", 0);
        if (num >= totalnum) {
            num = 0;
            // 循环了一圈了，该重新获取了（这是获取笑话的另外一种驱动方式）
            context.startService(new Intent(context, LoadJokesService.class));
        }
        editor.putInt("next_joke_num", num + 1);
        editor.commit();
        return num;
    }

    public static String getJoke(Context context, int num) {
        Logger.e("joke", "显示第" + num + "个笑话");
        SharedPreferences pre = context.getSharedPreferences("jokes", Context.MODE_PRIVATE);
        return pre.getString("joke_" + num, "暂无笑话");
    }

    public static void storeJokes(Context context, List<String> jokes) {
        Logger.e("joke", "存储笑话" + jokes.size() + "个");
        SharedPreferences pre = context.getSharedPreferences("jokes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        for (int i = 0; i < jokes.size(); i++) {
            editor.putString("joke_" + i, jokes.get(i));
        }
        int totalnum = pre.getInt("total_joke_num", 0);
        for (int i = totalnum - 1; i >= jokes.size(); i--) {
            editor.remove("joke_" + i);
        }
        editor.putInt("total_joke_num", jokes.size());
        editor.putInt("next_joke_num", 0);
        editor.commit();
        context.startService(new Intent(context, UpdateJokeService.class));
    }

}
