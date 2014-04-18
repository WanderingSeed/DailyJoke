package com.morgan.joke.data;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.morgan.joke.util.Logger;

/**
 * 用于存储和获取存储区的笑话。
 * 
 * @author Morgan.Ji
 * 
 */
public class JokePerference {

    public static int getCurrentJokeNum(Context context) {
        SharedPreferences pre = context.getSharedPreferences("jokes", Context.MODE_PRIVATE);
        return pre.getInt("current_joke_num", 0);
    }

    public static int getTotalJokeNum(Context context) {
        SharedPreferences pre = context.getSharedPreferences("jokes", Context.MODE_PRIVATE);
        return pre.getInt("total_joke_num", 0);
    }

    public static int getNextJokeNum(Context context) {
        SharedPreferences pre = context.getSharedPreferences("jokes", Context.MODE_PRIVATE);
        int num = pre.getInt("current_joke_num", 0);
        int totalnum = pre.getInt("total_joke_num", 0);
        num++;
        if (num >= totalnum) {
            num = 0;
        }
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("current_joke_num", num);
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
        editor.putInt("current_joke_num", 0);
        editor.commit();
    }

}
