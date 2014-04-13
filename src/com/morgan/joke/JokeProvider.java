package com.morgan.joke;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

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

	public static final int LOAD_JOKE_REQUEST_CODE = 2;
	public static final int UPDATE_WIDGET_REQUEST_CODE = 3;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onEnabled(Context context) {
		Logger.e("joke", "第一个widget被创建");
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		int num = JokePerference.getNextJokeNum(context);
		for (int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.joke_widget);
			views.setTextViewText(R.id.joke_text,
					JokePerference.getJoke(context, num));
			views.setTextColor(R.id.joke_text,
					SettingActivity.getTextColor(context));
			views.setFloat(R.id.joke_text, "setTextSize",
					SettingActivity.getTextSize(context));
			views.setImageViewBitmap(
					R.id.joke_bg,
					ImageUtils.toRoundBitmap(
							SettingActivity.getBackgroundColor(context), 16));
			Intent intent = new Intent(context, UpdateJokeService.class);
			views.setOnClickPendingIntent(R.id.nextjoke, PendingIntent
					.getService(context, UPDATE_WIDGET_REQUEST_CODE, intent,
							PendingIntent.FLAG_CANCEL_CURRENT));
			intent = new Intent(context, LoadJokesService.class);
			views.setOnClickPendingIntent(R.id.loadjoke, PendingIntent
					.getService(context, LOAD_JOKE_REQUEST_CODE, intent,
							PendingIntent.FLAG_CANCEL_CURRENT));
			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
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
