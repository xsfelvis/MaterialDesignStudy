package xsf.athena.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

import xsf.athena.BuildConfig;

/**
 * Author: xsf
 * Time: created at 2016/4/22.
 * Email: xsf_uestc_ncl@163.com
 */
public class LogUtil {
    public static final boolean isDebugMode = BuildConfig.DEBUG;

    //public static final boolean isDebugMode = false;
    public static void d(String message, Object... args) {
        if (isDebugMode) {
            Logger.d(message, args);
        }/*else  Log.d("xsf","shibai");*/
    }

    public static void d(String tag, String msg) {
        if (isDebugMode) {
            Log.d(tag, msg);
        }
    }

}
