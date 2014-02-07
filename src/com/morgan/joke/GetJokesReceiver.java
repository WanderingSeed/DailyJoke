package com.morgan.joke;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GetJokesReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent)
    {
        Log.e("joke", "获取笑话的BroadcastReceiver被唤醒");
        if (intent.getAction().equals("getjoke")) {
            new Thread() {
                public void run()
                {
                    while (true) {
                        List<String> jokes = new APiClient().getJokeList();
                        if (jokes.size() > 0) {
                            JokePerference.storeJokes(context, jokes);
                            break;
                        }
                        try {
                            Log.e("joke", "休息0.5秒，准备重新获取");
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }.start();
        }
    }

}
