package com.morgan.joke.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.morgan.joke.JokeProvider;
import com.morgan.joke.R;
import com.morgan.joke.data.JokePerference;
import com.morgan.joke.setting.SettingActivity;
import com.morgan.joke.util.ImageUtils;
import com.morgan.joke.util.Logger;

/**
 * 更新Widget组件上的笑话，更新方法在onStartCommand中调用，保证每次点击按钮都有效果。
 * 这样更新会导致点击下一个按钮会更新全部的Widget.
 * @author Morgan.Ji
 * 
 */
@Deprecated
public class UpdateJokeService extends Service {

    @Override
    public void onCreate() {
        Logger.e("joke", "更新笑话的service被创建");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Logger.e("joke", "更新笑话的service被销毁");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.e("joke", "更新笑话的service被调用");
        updateWidget();
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWidget() {
        int num = JokePerference.getCurrentJokeNum(this);
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.joke_widget);
        view.setTextViewText(R.id.joke_text, JokePerference.getJoke(this, num));
        view.setTextColor(R.id.joke_text, SettingActivity.getTextColor(UpdateJokeService.this));
        view.setFloat(R.id.joke_text, "setTextSize", SettingActivity.getTextSize(UpdateJokeService.this));
        view.setImageViewBitmap(R.id.joke_bg,
                ImageUtils.toRoundBitmap(SettingActivity.getBackgroundColor(UpdateJokeService.this), 16));
        Intent intent = new Intent(this, UpdateJokeService.class);
        view.setOnClickPendingIntent(R.id.nextjoke, PendingIntent.getService(this, JokeProvider.NEXT_JOKE_REQUEST_CODE,
                intent, PendingIntent.FLAG_CANCEL_CURRENT));
        intent = new Intent(this, LoadJokesService.class);
        view.setOnClickPendingIntent(R.id.loadjoke, PendingIntent.getService(this, JokeProvider.LOAD_JOKE_REQUEST_CODE,
                intent, PendingIntent.FLAG_CANCEL_CURRENT));
        ComponentName thisWidget = new ComponentName(this, JokeProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, view);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
