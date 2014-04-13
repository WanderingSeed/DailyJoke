package com.morgan.joke.setting;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.morgan.joke.Constant;
import com.morgan.joke.JokePerference;
import com.morgan.joke.JokeProvider;
import com.morgan.joke.LoadJokesService;
import com.morgan.joke.R;
import com.morgan.joke.UpdateJokeService;
import com.morgan.joke.util.ImageUtils;

/**
 * 用于设置关于笑话组件的一些可设置元素。
 * 
 * @author Morgan.Ji
 * 
 */
@SuppressWarnings("deprecation")
public class SettingActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static final String LOAD_JOKE_COUNT = "load_joke_number";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.activity_setting);
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		updateUI();
	}

	private void updateUI() {
		findPreference(LOAD_JOKE_COUNT)
				.setSummary(getLoadJokeNumber(this) + "");
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		if (preference.getKey().equals("reset")) {
			final AlertDialog dialog = new AlertDialog.Builder(this)
					.setTitle(R.string.tip).setMessage(R.string.are_you_sure_reset)
					.setPositiveButton(R.string.sure, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SharedPreferences preferences = getPreferenceScreen()
									.getSharedPreferences();
							Editor editor = preferences.edit();
							editor.clear();
							editor.commit();
							updateUI();
							onContentChanged();
							updateWidget();
						}
					}).setNegativeButton(R.string.cancel, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).create();
			dialog.show();
			return true;
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(LOAD_JOKE_COUNT)) {
			findPreference(key).setSummary(getLoadJokeNumber(this) + "");
		}
		updateWidget();
	}

	public static int getLoadJokeNumber(Context context) {
		int number;
		try {
			number = Integer.valueOf(PreferenceManager
					.getDefaultSharedPreferences(context).getString(
							LOAD_JOKE_COUNT, Constant.DEFAULT_JOKE_COUNT + ""));
		} catch (Exception e) {
			number = Constant.DEFAULT_JOKE_COUNT;
		}
		return number;
	}

	public static int getTextColor(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				"text_color", Constant.DEFAULT_TEXT_COLOR);
	}

	public static int getTextSize(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				"text_size", Constant.DEFAULT_TEXT_SIZE);
	}

	public static int getBackgroundColor(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				"bg_color", Constant.DEFAULT_BACKGROUND_COLOR);
	}
	

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 3000) {
            Toast.makeText(getApplicationContext(), R.string.another_click_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
    
	private void updateWidget() {
		int num = JokePerference.getCurrentJokeNum(this);
		RemoteViews view = new RemoteViews(getPackageName(),
				R.layout.joke_widget);
		view.setTextViewText(R.id.joke_text, JokePerference.getJoke(this, num));
		view.setTextColor(R.id.joke_text,
				SettingActivity.getTextColor(SettingActivity.this));
		view.setFloat(R.id.joke_text, "setTextSize",
				SettingActivity.getTextSize(SettingActivity.this));
		view.setImageViewBitmap(R.id.joke_bg, ImageUtils.toRoundBitmap(
				SettingActivity.getBackgroundColor(SettingActivity.this), 16));
		Intent intent = new Intent(this, UpdateJokeService.class);
		view.setOnClickPendingIntent(R.id.nextjoke, PendingIntent.getService(
				this, JokeProvider.UPDATE_WIDGET_REQUEST_CODE, intent,
				PendingIntent.FLAG_CANCEL_CURRENT));
		intent = new Intent(this, LoadJokesService.class);
		view.setOnClickPendingIntent(R.id.loadjoke, PendingIntent.getService(
				this, JokeProvider.LOAD_JOKE_REQUEST_CODE, intent,
				PendingIntent.FLAG_CANCEL_CURRENT));
		ComponentName thisWidget = new ComponentName(this, JokeProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(thisWidget, view);
	}
}
