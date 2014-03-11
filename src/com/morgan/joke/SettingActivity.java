package com.morgan.joke;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * 用于设置关于笑话组件的一些可设置元素。
 * 
 * @author Morgan.Ji
 * 
 */
public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            JokePerference.storeJokes(SettingActivity.this, new APiClient().getJokeList());
            super.run();
        }
    }
}
