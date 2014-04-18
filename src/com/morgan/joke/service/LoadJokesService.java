package com.morgan.joke.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.morgan.joke.JokeProvider;
import com.morgan.joke.data.JokePerference;
import com.morgan.joke.net.APiClient;
import com.morgan.joke.setting.SettingActivity;
import com.morgan.joke.util.Logger;

/**
 * 加载笑话的service，加载方法在onCreate中调用，防止多次启动。
 * 
 * @author Morgan.Ji
 * 
 */
public class LoadJokesService extends Service {

    private List<Integer> appWidgetIds = new ArrayList<Integer>();

    @Override
    public void onCreate() {
        Logger.e("joke", "获取笑话的service被启动");
        load();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Logger.e("joke", "获取笑话的service被销毁");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            int id = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int[] ids = bundle.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if (id != AppWidgetManager.INVALID_APPWIDGET_ID && !appWidgetIds.contains(id)) {
                appWidgetIds.add(id);
            }
            if (null != ids) {
                for (int value : ids) {
                    if (!appWidgetIds.contains(value)) {
                        appWidgetIds.add(value);
                    }
                }
            }
        }
        Logger.e("joke", "获取笑话的service被调用 " + appWidgetIds.toString());
        return START_REDELIVER_INTENT;
    }

    public void load() {
        new Thread() {
            public void run() {
                while (true) {
                    int number = SettingActivity.getLoadJokeNumber(LoadJokesService.this);
                    Logger.e("joke", "准备获取笑话" + number + "个");
                    List<String> jokes = new APiClient().getJokeList(number);
                    if (jokes.size() > 0) {
                        JokePerference.storeJokes(LoadJokesService.this, jokes);
                        Intent intent = new Intent(LoadJokesService.this, JokeProvider.class);
                        intent.setAction(JokeProvider.ACTION_NEXT_PAGE);
                        intent.putExtra(JokeProvider.PAGE_NUMBER, 0);
                        for (int i = 0; i < appWidgetIds.size(); i++) {
                            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds.get(i));
                            LoadJokesService.this.sendBroadcast(intent);
                        }
                        stopSelf();
                        break;
                    }
                    try {
                        Logger.e("joke", "休息1秒，准备重新获取 " + number);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
