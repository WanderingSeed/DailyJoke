package com.morgan.joke;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateJokeService extends Service {

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e("joke", "更新笑话的service被启动");
        update();
        return super.onStartCommand(intent, flags, startId);
    }

    private void update()
    {
        int num = JokePerference.getNextJokeNum(this);
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.joke_widget);
        view.setTextViewText(R.id.joke_text, JokePerference.getJoke(this, num));
        ComponentName thisWidget = new ComponentName(this, JokeProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, view);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
