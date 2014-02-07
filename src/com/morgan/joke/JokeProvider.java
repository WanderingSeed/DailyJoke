package com.morgan.joke;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class JokeProvider extends AppWidgetProvider {

    private static final int GET_JOKE_REQUEST_CODE = 2;
    private static final int UPDATE_WIDGET_REQUEST_CODE = 3;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context)
    {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        int num = JokePerference.getNextJokeNum(context);
        for (int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.joke_widget);
            views.setTextViewText(R.id.joke_text, JokePerference.getJoke(context, num));
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
        Intent intent = new Intent(context, GetJokesReceiver.class);
        intent.setAction("getjoke");
        PendingIntent sender = PendingIntent.getBroadcast(context, GET_JOKE_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 60 * 1000, sender);
        Log.e("joke", "设置获取笑话的唤醒时钟");
        final Intent i = new Intent(context, UpdateJokeService.class);
        PendingIntent service = PendingIntent.getService(context, UPDATE_WIDGET_REQUEST_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
        //多加4秒是为了以后显示第一个笑话的时候已经获取到新的笑话
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3 * 60 * 1000 + 4, 3 * 60 * 1000, service);
        Log.e("joke", "设置更新笑话的唤醒时钟");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        Log.e("joke", "一个widget被销毁");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context)
    {
        Log.e("joke", "widget被清空");
        Intent intent = new Intent(context, GetJokesReceiver.class);
        intent.setAction("getjoke");
        PendingIntent sender = PendingIntent.getBroadcast(context, GET_JOKE_REQUEST_CODE, intent, 0);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        Log.e("joke", "销毁获取笑话的唤醒时钟");
        alarm.cancel(sender);
        final Intent i = new Intent(context, UpdateJokeService.class);
        PendingIntent service = PendingIntent.getService(context, UPDATE_WIDGET_REQUEST_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
        Log.e("joke", "销毁更新笑话的唤醒时钟");
        alarm.cancel(service);
        super.onDisabled(context);
    }
}
