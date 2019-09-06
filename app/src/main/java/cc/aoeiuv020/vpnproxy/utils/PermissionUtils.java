package cc.aoeiuv020.vpnproxy.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.aoeiuv020.vpnproxy.BuildConfig;
import cc.aoeiuv020.vpnproxy.R;
import cc.aoeiuv020.vpnproxy.ui.JumpPageActivity;
import cc.aoeiuv020.vpnproxy.utils.suspensionWindowPermission.FloatWindowManager;

public class PermissionUtils {
    public static Map<String, String> map = new HashMap<>();
    public static PermissionUtils permissionUtils;
    public static int number = 0;
    private static List<String> listPermission = new ArrayList<>();
    public static String[] lsit = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
    };
    public static String[] firstLsit = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.SYSTEM_ALERT_WINDOW,
    };

    public static PermissionUtils getInstence() {
        if (permissionUtils == null) {
            getPermissionStrHashMap();
            permissionUtils = new PermissionUtils();
        }
        return permissionUtils;
    }

    /**
     * @param activity
     * @param list     权限数组
     * @return
     */
    public void permission(Activity activity, String[] list, IPermissionCallBack callback) {
        listPermission.clear();
        RxPermissions rxPermission = new RxPermissions((FragmentActivity) activity);
        final int[] isPemmission = {0};//判断权限  当有权限则++
        number = 0;
        final boolean[] isTrue = {true};
        rxPermission
                .requestEach(list
                )
                .subscribe(permission -> { // will emit 2 Permission objects
                    number++;
                    if (permission.granted) {
                        isPemmission[0] = isPemmission[0] + 1;
//                        Log.d("hxl", permission.name + " 用户已经同意该权限is granted.");
                        // `permission.name` is granted !允许。名称已被授予！
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        listPermission.add(map.get(permission.name));
//                        Log.d("hxl", permission.name + " 用户拒绝了该权限，没有选中『不再询问』is denied. More info should be provided.");
                        // Denied permission without ask never again  用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        //为了帮助查找用户可能需要解释的情形，Android 提供了一个实用程序方法，即 shouldShowRequestPermissionRationale()。如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
                        //注：如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don’t ask again 选项，此方法将返回 false。如果设备规范禁止应用具有该权限，此方法也会返回 false
                    } else {
                        listPermission.add(map.get(permission.name));
//                        Log.e("hxl", permission.name + " 用户拒绝了该权限，并且选中『不再询问』");
                        // Denied permission with ask never again 拒绝请求再也不允许	// 用户拒绝了该权限，并且选中『不再询问』
                        // Need to go to the settings //需要去设置
                    }
                    if (listPermission.indexOf("定位") > -1 && (listPermission.indexOf("定位") != listPermission.lastIndexOf("定位"))) {
                        listPermission.remove(listPermission.lastIndexOf("定位"));
                    }
                    if (number >= list.length && Arrays.toString(list).contains("android.permission.SYSTEM_ALERT_WINDOW")) {
                        isWind(activity);
                        return;
                    }
                    if (listPermission.size() > 0 && number == list.length) {
                        refuseShow(activity);
                    }

                    boolean permissions = isPemmission[0] >= list.length ? true : false;
                    if (callback != null && permissions && isTrue[0]) {
                        callback.callback();
                        isPemmission[0] = 0;
                    }
                });
    }

    //如果用户勾选拒绝权限后弹出的提示
    public static void toPerssionMainActivityLocation(Activity activity) {
        CustomDialog.getInstance().baseDialog(activity, new IDialogCallBack() {
                    @Override
                    public void determine(AlertDialog dialog,String mes) {
                        JumpPageActivity mainActivity = (JumpPageActivity) activity;
                        dialog.dismiss();
                    }

                    @Override
                    public void cancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                }, activity.getResources().getString(R.string.permission_prompt), activity.getResources().getString(R.string.permission_not_is_location_gps)
        );
    }

    //如果拒绝则弹出提示
    public static void refuseShow(Activity activity) {
        listPermission.remove(null);
        CustomDialog.getInstance().baseDialog(activity, new IDialogCallBack() {
                    @Override
                    public void determine(AlertDialog dialog,String msg) {
                        toSettings(activity);
                        dialog.dismiss();
                    }

                    @Override
                    public void cancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                }, activity.getResources().getString(R.string.permission_prompt), activity.getResources().getString(R.string.permission_not_is_continue_top)
                        + listPermission.toString() + activity.getResources().getString(R.string.permission_not_is_continue_bottom)
        );
    }

    public static void getPermissionStrHashMap() {
        map.put("android.permission.ACCESS_COARSE_LOCATION", "定位");
        map.put("android.permission.CAMERA", "相机");
        map.put("android.permission.WRITE_EXTERNAL_STORAGE", "存储");
        map.put("android.permission.READ_PHONE_STATE", "电话");
        map.put("android.permission.SYSTEM_OVERLAY_WINDOW", "悬浮窗");
        map.put("android.permission.SYSTEM_ALERT_WINDOW", "悬浮窗");
        map.put("android.permission.READ_EXTERNAL_STORAGE", "读SD卡");
        map.put("android.permission.ACCESS_FINE_LOCATION", "定位");
    }


    /**
     * 通过尝试打开相机的方式判断有无拍照权限（在6.0以下使用拥有root权限的管理软件可以管理权限）
     *
     * @return
     */
    public static boolean cameraIsCanUse(Activity activity) {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
            listPermission.clear();
            listPermission.add("相机");
            refuseShow(activity);
            return isCanUse;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                if (!isCanUse) {
                    refuseShow(activity);
                }
                return isCanUse;
            }
        }
        if (!isCanUse) {
            refuseShow(activity);
        }
        return isCanUse;
    }

    //去设置权限界面
    public static void toSettings(Activity activity) {
        //如果只剩下悬浮窗权限则
        if (listPermission.size() == 1 && listPermission.get(0).contains("悬浮窗")) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//            intent.setData(Uri.parse("package:" + activity.getPackageName()));
//            try{
//                activity.startActivityForResult(intent, SUSPENSION_WINDOW_PERMISSION_CALLBACK);
//            }catch(Exception e){
//                e.printStackTrace();
            FloatWindowManager.getInstance().applyOrShowFloatWindow(activity, false);
//            }


            return;
        }
        String brand = Build.BRAND;//手机厂商
        if (TextUtils.equals(brand.toLowerCase(), "redmi") || TextUtils.equals(brand.toLowerCase(), "xiaomi")) {
            gotoMiuiPermission(activity);//小米
        } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
            gotoMeizuPermission(activity);
        } else if (TextUtils.equals(brand.toLowerCase(), "huawei") || TextUtils.equals(brand.toLowerCase(), "honor")) {
            gotoHuaweiPermission(activity);
        } else {
            Intent intent = getAppDetailSettingIntent(activity);
            if (intent != null) {
                activity.startActivity(intent);
            }
        }
    }


    /**
     * 跳转到miui的权限管理页面
     */
    private static void gotoMiuiPermission(Activity activity) {
        try { // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", activity.getPackageName());
            activity.startActivity(localIntent);
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", activity.getPackageName());
                activity.startActivity(localIntent);
            } catch (Exception e1) { // 否则跳转到应用详情
                Intent intent = getAppDetailSettingIntent(activity);
                if (intent != null) {
                    activity.startActivity(intent);
                }
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private static void gotoMeizuPermission(Activity activity) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = getAppDetailSettingIntent(activity);
            if (intent != null) {
                activity.startActivity(intent);
            }
        }
    }

    /**
     * 华为的权限管理页面
     */
    private static void gotoHuaweiPermission(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = getAppDetailSettingIntent(activity);
            if (intent != null) {
                activity.startActivity(intent);
            }
        }

    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        //如果只剩下悬浮窗权限则
        if (listPermission.size() == 1 && listPermission.get(0).contains("悬浮窗")) {
            FloatWindowManager.getInstance().applyOrShowFloatWindow((JumpPageActivity) activity, false);
            return null;
        }
        return localIntent;
    }


    @SuppressLint("NewApi")
    public static boolean isWind(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (getAppOps(activity)) {//判断悬浮窗权限
            return true;
        }
//        }
        listPermission.clear();
        listPermission.add("悬浮窗");
        refuseShow(activity);
        return false;
    }


    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean getAppOps(Context context) {
        try {
            Object object = context.getSystemService("appops");
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }


    // Gps是否可用
    public boolean isGpsEnable(Activity activity) {
        LocationManager locationManager =
                ((LocationManager) activity.getSystemService(Context.LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // Wifi是否可用
    public boolean isWifiEnable(Activity activity) {
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }


    // 是否有可用网络
    public boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network != null) {
            return network.isAvailable();
        }
        return false;
    }


}
