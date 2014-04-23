package com.morgan.joke;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.morgan.joke.data.JokePerference;
import com.morgan.joke.service.LoadJokesService;
import com.morgan.joke.setting.SettingActivity;
import com.morgan.joke.util.ImageUtils;
import com.morgan.joke.util.Logger;

/**
 * 一个显示笑话的组件。
 * 
 * @author Morgan.Ji
 * 
 */
public class JokeProvider extends AppWidgetProvider {

    public static final String ACTION_NEXT_JOKE = "nextJoke";
    public static final String ACTION_NEXT_PAGE = "nextPage";
    public static final String PAGE_NUMBER = "pageNumber";
    public static final int NO_NEXT_PAGE = -1;
    public static final int LOAD_JOKE_REQUEST_CODE = 2;
    public static final int NEXT_JOKE_REQUEST_CODE = 3;
    public static final int NEXT_PAGE_REQUEST_CODE = 4;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        if (ACTION_NEXT_JOKE.equals(action)) {
            if (null != extras) {
                int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                int num = JokePerference.getNextJokeNum(context);
                if (num == 0) {
                    // 循环了一圈了，该重新获取了（这是获取笑话的另外一种驱动方式）
                    Intent loadIntent = new Intent(context, LoadJokesService.class);
                    loadIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                    context.startService(loadIntent);
                }
                String joke = JokePerference.getJoke(context, num);
                int nextPageNum = NO_NEXT_PAGE;
                if (joke.length() > SettingActivity.getPageSize(context)) {
                    joke = joke.substring(0, SettingActivity.getPageSize(context));
                    nextPageNum = 1;
                }
                updateJoke(context, appWidgetId, joke, nextPageNum);
            }
        } else if (ACTION_NEXT_PAGE.equals(action)) {
            if (null != extras) {
                int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                int pageNum = extras.getInt(PAGE_NUMBER, NO_NEXT_PAGE);
                if (pageNum != NO_NEXT_PAGE) {
                    int num = JokePerference.getCurrentJokeNum(context);
                    String joke = JokePerference.getJoke(context, num);
                    joke = joke.substring(pageNum * SettingActivity.getPageSize(context));
                    int nextPageNum = NO_NEXT_PAGE;
                    if (joke.length() > SettingActivity.getPageSize(context)) {
                        joke = joke.substring(0, SettingActivity.getPageSize(context));
                        nextPageNum = pageNum + 1;
                    }
                    updateJoke(context, appWidgetId, joke, nextPageNum);
                }
            }
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Logger.e("joke", "第一个widget被创建");
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        if (JokePerference.getTotalJokeNum(context) == 0) {
            Intent intent = new Intent(context, LoadJokesService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            context.startService(intent);
        } else {
            int num = JokePerference.getCurrentJokeNum(context);
            String joke = JokePerference.getJoke(context, num);
            int nextPageNum = NO_NEXT_PAGE;
            if (joke.length() > SettingActivity.getPageSize(context)) {
                joke = joke.substring(0, SettingActivity.getPageSize(context));
                nextPageNum = 1;
            }
            for (int i = 0; i < appWidgetIds.length; i++) {
                updateJoke(context, appWidgetIds[i], joke, nextPageNum);
            }
        }
    }

    private void updateJoke(Context context, int appWidgetId, String joke, int nextPageNum) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.joke_widget);
        views.setTextViewText(R.id.joke_text, joke);
        views.setTextColor(R.id.joke_text, SettingActivity.getTextColor(context));
        views.setFloat(R.id.joke_text, "setTextSize", SettingActivity.getTextSize(context));
        views.setImageViewBitmap(R.id.joke_bg,
                ImageUtils.toRoundBitmap(SettingActivity.getBackgroundColor(context), 16));
        Intent intent = new Intent(context, JokeProvider.class);
        intent.setAction(ACTION_NEXT_JOKE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setOnClickPendingIntent(R.id.nextjoke,
                PendingIntent.getBroadcast(context, NEXT_JOKE_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT));
        if (nextPageNum != NO_NEXT_PAGE) {
            intent = new Intent(context, JokeProvider.class);
            intent.setAction(ACTION_NEXT_PAGE);
            intent.putExtra(PAGE_NUMBER, nextPageNum);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            views.setViewVisibility(R.id.nextPage, View.VISIBLE);
            views.setOnClickPendingIntent(R.id.nextPage, PendingIntent.getBroadcast(context, NEXT_PAGE_REQUEST_CODE,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT));
        } else {
            views.setViewVisibility(R.id.nextPage, View.GONE);
        }
        intent = new Intent(context, LoadJokesService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setOnClickPendingIntent(R.id.loadjoke,
                PendingIntent.getService(context, LOAD_JOKE_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Logger.e("joke", "一个widget被销毁");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Logger.e("joke", "widget被清空");
        super.onDisabled(context);
    }
}
