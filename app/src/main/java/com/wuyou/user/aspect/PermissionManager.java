package com.wuyou.user.aspect;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by DELL on 2018/6/22.
 */

public class PermissionManager {

    private static PermissionManager permissionManager;

    private PermissionManager() {
    }

    public static synchronized PermissionManager getIns() {
        if (permissionManager == null) {
            return new PermissionManager();
        }
        return permissionManager;
    }

    public boolean checkPermission(Context context, String permission) {
        int selfPermission = ContextCompat.checkSelfPermission(context, permission);
        if (selfPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (selfPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{permission}, 1);
            return false;
        }
        return false;
    }


}
