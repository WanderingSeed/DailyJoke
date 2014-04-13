package com.morgan.joke;

import java.util.List;

import com.morgan.joke.setting.SettingActivity;
import com.morgan.joke.util.Logger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 加载笑话的service，加载方法在onCreate中调用，防止多次启动。
 * 
 * @author Morgan.Ji
 * 
 */
public class LoadJokesService extends Service {

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
        Logger.e("joke", "获取笑话的service被调用");
        return super.onStartCommand(intent, flags, startId);
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
