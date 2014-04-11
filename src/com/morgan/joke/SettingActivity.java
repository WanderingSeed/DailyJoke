package com.morgan.joke;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/**
 * 用于设置关于笑话组件的一些可设置元素。
 * 
 * @author Morgan.Ji
 * 
 */
@SuppressWarnings("deprecation")
public class SettingActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_setting);
        findPreference("load_joke_number").setSummary(
                getPreferenceScreen().getSharedPreferences()
                        .getString("load_joke_number", Constant.GET_JOKE_COUNT + ""));
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if (preference.getKey().equals("reset")) {
            final AlertDialog dialog = new AlertDialog.Builder(this).setTitle("提示").setMessage("确认恢复默认设置吗？")
                    .setPositiveButton("确定", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // SharedPreferences preferences =
                            // getPreferences(MODE_PRIVATE);
                            // Editor editor = preferences.edit();
                            // editor.putString(key, value);
                            // editor.commit();
                        }
                    }).setNegativeButton("取消", new OnClickListener() {

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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("load_joke_number")) {
            findPreference(key).setSummary(
                    sharedPreferences.getString("load_joke_number", Constant.GET_JOKE_COUNT + ""));
        }
    }
}
