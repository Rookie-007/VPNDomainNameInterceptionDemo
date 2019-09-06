package cc.aoeiuv020.vpnproxy.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import cc.aoeiuv020.vpnproxy.R;

public class Utils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;
//    private static final int FAKE_STATUSBAR_ID = R.id.fake_status_bar_view;

    public static void d(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.d(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.d(tag, msg);
    }

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    public static boolean isShouldHideKeyboard(MotionEvent event, View... collection) {
        if (collection != null && collection.length > 0) {
            for (View view : collection) {
                int[] l = {0, 0};
                view.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + view.getHeight(),
                        right = left + view.getWidth();
                if (!(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }



    /*
     * 全透状态栏
     */
    private static void setStatusBarFullTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public static void setColor(Activity activity, int color, boolean isDrawable, @DrawableRes int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
// 设置状态栏透明
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// 添加 statusView 到布局中
//            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
//在一个界面在来回切换顶部状态栏的时候导致透明度的状态栏不能显示 需remove掉

            setStatusBarFullTransparent(activity);
//            BarUtils.setStatusBarCustom(createStatusView(activity,color,isDrawable,res));
            View view = activity.findViewById(R.id.fake_status_bar);
            if (view != null) {
                int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
                int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = BarUtils.getStatusBarHeight();
                view.setLayoutParams(params);
                view.setVisibility(View.VISIBLE);
            }
//            ViewGroup decorViewGroup = (ViewGroup) activity.getWindow().getDecorView();
//            View statusBarView = decorViewGroup.findViewWithTag(R.id.fake_status_bar_view);
//            if (statusBarView == null) {
//                statusBarView = new StatusBarView(activity);
//                statusBarView.setTag(R.id.fake_status_bar_view);
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                params.gravity = Gravity.TOP;
//                statusBarView.setLayoutParams(params);
//                decorViewGroup.addView(statusBarView);
//            }
//            if (isDrawable) {
//                statusBarView.setBackground(activity.getResources().getDrawable(R.drawable.fake_status_bar_bg));
//            } else {
//                statusBarView.setBackgroundColor(color);
//            }
// 设置根布局的参数
//            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
//            rootView.setFitsSystemWindows(true);
//            rootView.setClipToPadding(false);
        } else {
            StatusBarCompat.setStatusBarColor(activity, color);
        }
    }

    /**
     * @param Version1 第一个版本号
     * @param Version2 第二个版本号
     * @return int Version1和Version2对比，大于等于小于分别为1,0,-1
     */
    public static int compareVersion(String Version1, String Version2) {
        return compareVersion(Version1, Version2, "\\.");
    }

    //版本号对比，以.为分割标识，Version1和Version2对比
    private static int compareVersion(String Version1, String Version2, String reg) {
        if (Version1.isEmpty() && Version2.isEmpty()) {
            return 0;
        }
        if (Version1.isEmpty() && !Version2.isEmpty()) {
            return -1;
        }
        if (!Version1.isEmpty() && Version2.isEmpty()) {
            return 1;
        }
        String[] vas1 = Version1.split(reg);
        String[] vas2 = Version2.split(reg);
        int maxLength = Math.max(vas1.length, vas2.length);
        ArrayList<String> ars1 = new ArrayList<>();
        ArrayList<String> ars2 = new ArrayList<>();
        for (int i = 0; i < vas1.length; i++) {
            ars1.add(vas1[i]);
        }
        for (int i = 0; i < vas2.length; i++) {
            ars2.add(vas2[i]);
        }
        if (maxLength > ars1.size()) {
            for (int i = ars1.size(); i < maxLength; i++) {
                ars1.add("0");
            }
        }

        if (maxLength > ars2.size()) {
            for (int i = ars2.size(); i < maxLength; i++) {
                ars2.add("0");
            }
        }
        int index = 0;
        while (index < maxLength) {
            if (Integer.parseInt(ars1.get(index)) > Integer.parseInt(ars2.get(index))) {
                return 1;
            }
            if (Integer.parseInt(ars1.get(index)) < Integer.parseInt(ars2.get(index))) {
                return -1;
            }
            index++;
        }
        return 0;
    }


    public static void setTextViewDrawIcon(TextView view, Drawable drawable, int width, int height) {
        view.setCompoundDrawablePadding(0);
        drawable.setBounds(0, 0, width, height);
        view.setCompoundDrawables(drawable, null, null, null);
    }

    public static void setTextViewDrawIcon(Context context, TextView view, @DrawableRes int drawrs, int width, int height) {
        Drawable drawable = context.getResources().getDrawable(drawrs);
        setTextViewDrawIcon(view, drawable, width, height);
    }


    /**
     * 获取进程名
     *
     * @param cxt
     * @param pid
     * @return
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

}