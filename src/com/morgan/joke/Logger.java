package com.morgan.joke;

import java.io.File;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

/**
 * 该类是为了开发android应用时更好的调试及记录应用产生的错误信息，在开发时设置debug为true,信息会在logcat中显示出来，
 * 安装到手机上时设置debug为false, 信息会记录在SD卡上的log文件中
 * 
 * @author Morgan.Ji
 * 
 */
public class Logger {

    private static boolean DEBUG = false;
    private static final int mStoreLevel = Log.VERBOSE;
    private static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator +"joke"+ File.separator + "log.txt";
    private static final String DEFAULT_TAG = "default";

    static {
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator +"joke");
        if (!path.exists()) {
            if (path.mkdir()) {
                Log.e("joke", "cerate dir success");
            } else {
                Log.e("joke", "cerate dir fail");
            }
        }
    }
    /**
     * 在非debug模式下存储到文件的最低等级(共六个等级从verbose到assert(2到7))
     */

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(DEFAULT_TAG, msg);
        } else if (mStoreLevel <= Log.DEBUG) {
            writeFile(msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        } else if (mStoreLevel <= Log.DEBUG) {
            writeFile(tag + " " + msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.d(tag, msg, tr);
        } else if (mStoreLevel <= Log.DEBUG) {
            writeFile(tag + " " + msg + "/r/n" + tr.getMessage());
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(DEFAULT_TAG, msg);
        } else if (mStoreLevel <= Log.INFO) {
            writeFile(msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        } else if (mStoreLevel <= Log.INFO) {
            writeFile(tag + " " + msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.i(tag, msg, tr);
        } else if (mStoreLevel <= Log.INFO) {
            writeFile(tag + " " + msg + "/r/n" + tr.getMessage());
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(DEFAULT_TAG, msg);
        } else if (mStoreLevel <= Log.ERROR) {
            writeFile(msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        } else if (mStoreLevel <= Log.ERROR) {
            writeFile(tag + " " + msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.e(tag, msg, tr);
        } else if (mStoreLevel <= Log.ERROR) {
            writeFile(tag + " " + msg + "/r/n" + tr.getMessage());
        }
    }

    private static void writeFile(String msg) {
        Log.e(DEFAULT_TAG, msg);
        FileUtils.writeFile(LOG_FILE_PATH, new Date().toString() + " " + msg);
    }
}