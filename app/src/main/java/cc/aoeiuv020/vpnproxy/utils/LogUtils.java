package cc.aoeiuv020.vpnproxy.utils;

import android.util.Log;

/*
 Created by Little Q on date
 Page:
 Notes:
 Date: 2019/8/5
 Time: 16:12
 */public class LogUtils {
     private static String TAG =LogUtils.class.getName();
     private static boolean isDebug = true;

     public static void e(String message){
         if(isDebug){
             Log.e(TAG,message);
         }
     }

    public static void e(String tag,String message){
        if(isDebug){
            Log.e(tag,message);
        }
    }
}
