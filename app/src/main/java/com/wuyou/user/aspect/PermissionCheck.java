package com.wuyou.user.aspect;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by DELL on 2018/6/22.
 */
@Aspect
public class PermissionCheck {
    @Pointcut("execution(@com.wuyou.user.aspect.PermissionCheckAnnotation  * *(..))")
    public void executionAspectJ() {}

    @Around("executionAspectJ()")
    public Object aroundAspectJ(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        PermissionCheckAnnotation annotation = methodSignature.getMethod().getAnnotation(PermissionCheckAnnotation.class);
        String permission = annotation.value();
        Context context = ((Fragment) joinPoint.getThis()).getContext();
        Object o = null;
        return PermissionManager.getIns().checkPermission(context, permission);
    }
}
