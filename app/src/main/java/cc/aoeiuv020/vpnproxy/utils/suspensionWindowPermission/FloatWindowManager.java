package cc.aoeiuv020.vpnproxy.utils.suspensionWindowPermission;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import com.blankj.utilcode.util.LogUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cc.aoeiuv020.vpnproxy.R;
import cc.aoeiuv020.vpnproxy.utils.CustomDialog;
import cc.aoeiuv020.vpnproxy.utils.IDialogCallBack;
import cc.aoeiuv020.vpnproxy.utils.suspensionWindowPermission.rom.HuaweiUtils;
import cc.aoeiuv020.vpnproxy.utils.suspensionWindowPermission.rom.MeizuUtils;
import cc.aoeiuv020.vpnproxy.utils.suspensionWindowPermission.rom.MiuiUtils;
import cc.aoeiuv020.vpnproxy.utils.suspensionWindowPermission.rom.OppoUtils;
import cc.aoeiuv020.vpnproxy.utils.suspensionWindowPermission.rom.QikuUtils;
import cc.aoeiuv020.vpnproxy.utils.suspensionWindowPermission.rom.RomUtils;

/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-10-17
 */

public class FloatWindowManager {
    private static final String TAG = "FloatWindowManager";

    private static volatile FloatWindowManager instance;

    private boolean isWindowDismiss = true;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    private AVCallFloatView floatView = null;
    private Dialog dialog;

    public static FloatWindowManager getInstance() {
        if (instance == null) {
            synchronized (FloatWindowManager.class) {
                if (instance == null) {
                    instance = new FloatWindowManager();
                }
            }
        }
        return instance;
    }

    public void applyOrShowFloatWindow(Activity context, boolean isDialog) {
        if (checkPermission(context)) {
//            showWindow(context);
            LogUtils.d("ShowWindowsFloat");
        } else {
            applyPermission(context, isDialog);
        }
    }

    private boolean checkPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(context);
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    private boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private boolean oppoROMPermissionCheck(Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    private boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    private void applyPermission(Activity context, boolean isDialog) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply(context, isDialog);
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(context, isDialog);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply(context, isDialog);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(context, isDialog);
            } else if (RomUtils.checkIsOppoRom()) {
                oppoROMPermissionApply(context, isDialog);
            }
        } else {
            commonROMPermissionApply(context, isDialog);
        }
    }

    private void ROM360PermissionApply(final Activity context, boolean isDialog) {
        if (isDialog) {
            showConfirmDialog(context, new OnConfirmResult() {
                @Override
                public void confirmResult(boolean confirm) {
                    if (confirm) {
                        QikuUtils.applyPermission(context);
                    } else {
                        Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
                    }
                }
            });
        } else {
            QikuUtils.applyPermission(context);
        }

    }

    private void huaweiROMPermissionApply(final Activity context, boolean isDialog) {
        if (isDialog) {
            showConfirmDialog(context, new OnConfirmResult() {
                @Override
                public void confirmResult(boolean confirm) {
                    if (confirm) {
                        HuaweiUtils.applyPermission(context);
                    } else {
                        Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                    }
                }
            });
        } else {
            HuaweiUtils.applyPermission(context);
        }

    }

    private void meizuROMPermissionApply(final Activity context, boolean isDialog) {
        if (isDialog) {
            showConfirmDialog(context, new OnConfirmResult() {
                @Override
                public void confirmResult(boolean confirm) {
                    if (confirm) {
                        MeizuUtils.applyPermission(context);
                    } else {
                        Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                    }
                }
            });
        } else {
            MeizuUtils.applyPermission(context);
        }

    }

    private void miuiROMPermissionApply(final Activity context, boolean isDialog) {
        if (isDialog) {
            showConfirmDialog(context, new OnConfirmResult() {
                @Override
                public void confirmResult(boolean confirm) {
                    if (confirm) {
                        MiuiUtils.applyMiuiPermission(context);
                    } else {
                        Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                    }
                }
            });

        } else {
            MiuiUtils.applyMiuiPermission(context);
        }

    }

    private void oppoROMPermissionApply(final Activity context, boolean isDialog) {
        if (isDialog) {
            showConfirmDialog(context, new OnConfirmResult() {
                @Override
                public void confirmResult(boolean confirm) {
                    if (confirm) {
                        OppoUtils.applyOppoPermission(context);
                    } else {
                        Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                    }
                }
            });
        } else {
            OppoUtils.applyOppoPermission(context);
        }

    }

    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(final Activity context, boolean isDialog) {
        if (isDialog) {
            //这里也一样，魅族系统需要单独适配
            if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(context, isDialog);
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    showConfirmDialog(context, new OnConfirmResult() {
                        @Override
                        public void confirmResult(boolean confirm) {
                            if (confirm) {
                                try {
                                    commonROMPermissionApplyInternal(context);
                                } catch (Exception e) {
                                    Log.e(TAG, Log.getStackTraceString(e));
                                }
                            } else {
                                Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
                                //需要做统计效果
                            }
                        }
                    });
                }
            }
        } else {
            try {
                commonROMPermissionApplyInternal(context);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }

    }

    public static void commonROMPermissionApplyInternal(Context context) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = Settings.class;
        Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

        Intent intent = new Intent(field.get(null).toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    private void showConfirmDialog(Activity context, OnConfirmResult result) {
        showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    private void showConfirmDialog(Activity activity, String message, final OnConfirmResult result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        CustomDialog.getInstance().baseDialog(activity, new IDialogCallBack() {
            @Override
            public void determine(AlertDialog dialog, String websiteUrl) {
                result.confirmResult(true);
                dialog.dismiss();
            }

            @Override
            public void cancel(AlertDialog dialog) {
                dialog.dismiss();
            }
        },activity.getResources().getString(R.string.permission_prompt), activity.getResources().getString(R.string.permission_not_is_continue_top)
                + "悬浮窗" + activity.getResources().getString(R.string.permission_not_is_continue_bottom));


//        dialog = new AlertDialog.Builder(activity).setCancelable(true).setTitle("")
//                .setMessage(message)
//                .setPositiveButton("现在去开启",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                result.confirmResult(true);
//                                dialog.dismiss();
//                            }
//                        }).setNegativeButton("暂不开启",
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                result.confirmResult(false);
//                                dialog.dismiss();
//                            }
//                        }).create();
//        dialog.show();
    }

    private interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }

    private void showWindow(Context context) {
        if (!isWindowDismiss) {
            Log.e(TAG, "view is already added here");
            return;
        }
        LogUtils.d("ShowWindowsFloat");
        isWindowDismiss = false;
        if (windowManager == null) {
            windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }

        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        mParams = new WindowManager.LayoutParams();
        mParams.packageName = context.getPackageName();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        int mType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mType = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }
        mParams.type = mType;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.x = screenWidth - dp2px(context, 100);
        mParams.y = screenHeight - dp2px(context, 171);


//        ImageView imageView = new ImageView(mContext);
//        imageView.setImageResource(R.drawable.app_icon);
        floatView = new AVCallFloatView(context);
        floatView.setParams(mParams);
        floatView.setIsShowing(true);
        windowManager.addView(floatView, mParams);
    }

    public void dismissWindow() {
        if (isWindowDismiss) {
            Log.e(TAG, "window can not be dismiss cause it has not been added");
            return;
        }

        isWindowDismiss = true;
        floatView.setIsShowing(false);
        if (windowManager != null && floatView != null) {
            windowManager.removeViewImmediate(floatView);
        }
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
