package com.wuyou.android_user.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author cginechen
 * @date 2016-03-27
 */
public class QMUIStatusBarHelper {

    private final static int STATUSBAR_TYPE_DEFAULT = 0;
    private final static int STATUSBAR_TYPE_MIUI = 1;
    private final static int STATUSBAR_TYPE_FLYME = 2;
    private final static int STATUSBAR_TYPE_ANDROID6 = 3; // Android 6.0
    private final static int STATUS_BAR_DEFAULT_HEIGHT_DP = 25; // 大部分状态栏都是25dp
    // 在某些机子上存在不同的density值，�?以增加两个虚拟�??
    public static float sVirtualDensity = -1;
    public static float sVirtualDensityDpi = -1;
    private static int sStatusbarHeight = -1;
    private static
    @StatusBarType
    int mStatuBarType = STATUSBAR_TYPE_DEFAULT;
    private static Integer sTransparentValue;

    public static void translucent(Activity activity) {
        translucent(activity, 0x40000000);
    }

    /**
     * 沉浸式状态栏
     * 支持 4.4 以上版本�? MIUI �? Flyme，以�? 5.0 以上版本的其�? Android
     *
     * @param activity
     */
    @TargetApi(19)
    public static void translucent(Activity activity, @ColorInt int colorOn5x) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            // 版本小于4.4，绝对不考虑沉浸�?
            return;
        }
        // 小米和魅�?4.4 以上版本支持沉浸�?
        if (QMUIDeviceHelper.isMeizu() || QMUIDeviceHelper.isMIUI()) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportTransclentStatusBar6()) {
                // android 6以后可以改状态栏字体颜色，因此可以自行设置为透明
                // ZUK Z1是个另类，自家应用可以实现字体颜色变色，但没�?放接�?
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                // android 5不能修改状�?�栏字体颜色，因此直接用FLAG_TRANSLUCENT_STATUS，nexus表现为半透明
                // 魅族和小米的表现如何�?
                // update: 部分手机运用FLAG_TRANSLUCENT_STATUS时背景不是半透明而是没有背景了�?��?��?��?��??
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // 采取setStatusBarColor的方式，部分机型不支持，那就纯黑了，保证状�?�栏图标可见
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(colorOn5x);
            }
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // android4.4的默认是从上到下黑到透明，我们的背景是白色，很难看，因此只做魅族和小米的
//        } else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1){
//            // 如果app 为白色，�?要更改状态栏颜色，因此不能让19�?下支持�?�明状�?�栏
//            Window window = activity.getWindow();
//            Integer transparentValue = getStatusBarAPITransparentValue(activity);
//            if(transparentValue != null) {
//                window.getDecorView().setSystemUiVisibility(transparentValue);
//            }
        }
    }

    /**
     * 设置状�?�栏黑色字体图标�?
     * 支持 4.4 以上版本 MIUI �? Flyme，以�? 6.0 以上版本的其�? Android
     *
     * @param activity �?要被处理�? Activity
     */
    public static boolean setStatusBarLightMode(Activity activity) {
        // 无语系列：ZTK C2016只能时间和电池图标变色�?��?��?��??
        if (QMUIDeviceHelper.isZTKC2016()) {
            return false;
        }

        if (mStatuBarType != STATUSBAR_TYPE_DEFAULT) {
            return setStatusBarLightMode(activity, mStatuBarType);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMIUICustomStatusBarLightModeImpl() && MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                mStatuBarType = STATUSBAR_TYPE_MIUI;
                return true;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                mStatuBarType = STATUSBAR_TYPE_FLYME;
                return true;
            }
//            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Android6SetStatusBarLightMode(activity.getWindow(), false);
//                mStatuBarType = STATUSBAR_TYPE_ANDROID6;
//                return true;
//            }
        }
        return false;
    }

    /**
     * 已知系统类型时，设置状�?�栏黑色字体图标�?
     * 支持 4.4 以上版本 MIUI �? Flyme，以�? 6.0 以上版本的其�? Android
     *
     * @param activity �?要被处理�? Activity
     * @param type     StatusBar 类型，对应不同的系统
     */
    private static boolean setStatusBarLightMode(Activity activity, @StatusBarType int type) {
        if (type == STATUSBAR_TYPE_MIUI) {
            return MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == STATUSBAR_TYPE_FLYME) {
            return FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == STATUSBAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.getWindow(), true);
        }
        return false;
    }


    /**
     * 设置状�?�栏白色字体图标
     * 支持 4.4 以上版本 MIUI �? Flyme，以�? 6.0 以上版本的其�? Android
     */
    public static boolean setStatusBarDarkMode(Activity activity) {
        if (mStatuBarType == STATUSBAR_TYPE_DEFAULT) {
            // 默认状�?�，不需要处�?
            return true;
        }

        if (mStatuBarType == STATUSBAR_TYPE_MIUI) {
            return MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (mStatuBarType == STATUSBAR_TYPE_FLYME) {
            return FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (mStatuBarType == STATUSBAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.getWindow(), false);
        }
        return true;
    }

    @TargetApi(23)
    private static int changeStatusBarModeRetainFlag(Window window, int out) {
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        return out;
    }

    public static int retainSystemUiFlag(Window window, int out, int type) {
        int now = window.getDecorView().getSystemUiVisibility();
        if ((now & type) == type) {
            out |= type;
        }
        return out;
    }


    /**
     * 设置状�?�栏字体图标为深色，Android 6
     *
     * @param window �?要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @TargetApi(23)
    private static boolean Android6SetStatusBarLightMode(Window window, boolean light) {
        View decorView = window.getDecorView();
        int systemUi = light ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        systemUi = changeStatusBarModeRetainFlag(window, systemUi);
        decorView.setSystemUiVisibility(systemUi);
        return true;
    }

    /**
     * 设置状�?�栏字体图标为深色，�?�? MIUIV6 以上
     *
     * @param window �?要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回 true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状�?�栏透明且黑色字�?
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 更改状�?�栏图标、文字颜色的方案是否是MIUI自家的， MIUI9之后用回Android原生实现
     * 见小米开发文档说明：https://dev.mi.com/console/doc/detail?pId=1159
     *
     * @return
     */
    private static boolean isMIUICustomStatusBarLightModeImpl() {
        return QMUIDeviceHelper.isMIUIV5() || QMUIDeviceHelper.isMIUIV6() ||
                QMUIDeviceHelper.isMIUIV7() || QMUIDeviceHelper.isMIUIV8();
    }

    /**
     * 设置状�?�栏图标为深色和魅族特定的文字风�?
     * 可以用来判断是否�? Flyme 用户
     *
     * @param window �?要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 获取是否全屏
     *
     * @return 是否全屏
     */
    public static boolean isFullScreen(Activity activity) {
        boolean ret = false;
        try {
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            ret = (attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * API19之前透明状�?�栏：获取设置�?�明状�?�栏的system ui visibility的�?�，这是部分有提供接口的rom使用�?
     * http://stackoverflow.com/questions/21865621/transparent-status-bar-before-4-4-kitkat
     *
     * @return
     */
    public static Integer getStatusBarAPITransparentValue(Context context) {
        if (sTransparentValue != null) {
            return sTransparentValue;
        }
        String[] systemSharedLibraryNames = context.getPackageManager()
                .getSystemSharedLibraryNames();
        String fieldName = null;
        for (String lib : systemSharedLibraryNames) {
            if ("touchwiz".equals(lib)) {
                fieldName = "SYSTEM_UI_FLAG_TRANSPARENT_BACKGROUND";
            } else if (lib.startsWith("com.sonyericsson.navigationbar")) {
                fieldName = "SYSTEM_UI_FLAG_TRANSPARENT";
            }
        }

        if (fieldName != null) {
            try {
                Field field = View.class.getField(fieldName);
                if (field != null) {
                    Class<?> type = field.getType();
                    if (type == int.class) {
                        int value = field.getInt(null);
                        sTransparentValue = value;
                    }
                }
            } catch (Exception e) {
            }
        }
        return sTransparentValue;
    }

    /**
     * Android6.0是否可以启用 window.setStatusBarColor(Color.TRANSPARENT);
     *
     * @return
     */
    public static boolean supportTransclentStatusBar6() {
        return !(QMUIDeviceHelper.isZUKZ1() || QMUIDeviceHelper.isZTKC2016());
    }

    /**
     * 获取状�?�栏的高�?
     *
     * @param context
     * @return
     */
    public static int getStatusbarHeight(Context context) {
        if (sStatusbarHeight == -1) {
            initStatusBarHeight(context);
        }
        return sStatusbarHeight;
    }

    private static void initStatusBarHeight(Context context) {
        Class<?> clazz = null;
        Object obj = null;
        Field field = null;
        try {
            clazz = Class.forName("com.android.internal.R$dimen");
            obj = clazz.newInstance();
            if (QMUIDeviceHelper.isMeizu()) {
                try {
                    field = clazz.getField("status_bar_height_large");
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            if (field == null) {
                field = clazz.getField("status_bar_height");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (field != null && obj != null) {
            try {
                int id = Integer.parseInt(field.get(obj).toString());
                sStatusbarHeight = context.getResources().getDimensionPixelSize(id);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (QMUIDeviceHelper.isTablet(context)
                && sStatusbarHeight > DensityUtils.dip2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP)) {
            //状�?�栏高度大于25dp的平板，状�?�栏通常在下�?
            sStatusbarHeight = 0;
        } else {
            if (sStatusbarHeight <= 0
                    || sStatusbarHeight > DensityUtils.dip2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP * 2)) {
                //安卓默认状�?�栏高度�?25dp，如果获取的状�?�高度大�?2�?25dp的话，这个数值可能有问题，用回桌面定义的值从新获取�?�出现这种可能�?�较低，只有小部分手机出�?
                if (sVirtualDensity == -1) {
                    sStatusbarHeight = DensityUtils.dip2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP);
                } else {
                    sStatusbarHeight = (int) (STATUS_BAR_DEFAULT_HEIGHT_DP * sVirtualDensity + 0.5f);
                }
            }
        }
    }

    public static void setVirtualDensity(float density) {
        sVirtualDensity = density;
    }

    public static void setVirtualDensityDpi(float densityDpi) {
        sVirtualDensityDpi = densityDpi;
    }

    @IntDef({STATUSBAR_TYPE_DEFAULT, STATUSBAR_TYPE_MIUI, STATUSBAR_TYPE_FLYME, STATUSBAR_TYPE_ANDROID6})
    @Retention(RetentionPolicy.SOURCE)
    private @interface StatusBarType {
    }

}
