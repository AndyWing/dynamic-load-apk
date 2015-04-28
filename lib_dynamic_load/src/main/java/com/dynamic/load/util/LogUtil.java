package com.dynamic.load.util;

import android.util.Log;

/**
 * Created by Andy Wing on 15-4-24.
 */
public class LogUtil {

    private static final String LOG_TAG = "DynamicLoad";

    public static final boolean DEBUG = Debug.DEBUG;

    public static void d(String msg) {
        if (DEBUG) Log.d(LOG_TAG, msg);
    }
}
