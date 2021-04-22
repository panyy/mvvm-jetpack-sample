package com.common.arms.utils;

import android.Manifest;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import timber.log.Timber;

/**
 * ================================================
 * 权限请求工具类
 * ================================================
 */
public class PermissionUtil {
    public static final String TAG = "Permission";

    private PermissionUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static void requestPermission(FragmentActivity activity, PermissionCallBack callBack, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return;
        }

        RxPermissions rxPermissions = new RxPermissions(activity);
        RxErrorHandler errorHandler = RxErrorHandler
                .builder()
                .with(activity)
                .responseErrorListener((context, t) -> {
                }).build();

        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) { //过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }

        if (needRequest.isEmpty()) {//全部权限都已经申请过，直接执行操作
            callBack.onSuccess();
        } else {//没有申请过,则开始申请
            rxPermissions
                    .requestEach(needRequest.toArray(new String[0]))
                    .buffer(permissions.length)
                    .subscribe(new ErrorHandleSubscriber<List<Permission>>(errorHandler) {
                        @Override
                        public void onNext(@NonNull List<Permission> permissions) {
                            List<String> failurePermissions = new ArrayList<>();
                            List<String> askNeverAgainPermissions = new ArrayList<>();
                            for (Permission p : permissions) {
                                if (!p.granted) {
                                    if (p.shouldShowRequestPermissionRationale) {
                                        failurePermissions.add(p.name);
                                    } else {
                                        askNeverAgainPermissions.add(p.name);
                                    }
                                }
                            }
                            if (failurePermissions.size() > 0) {
                                Timber.tag(TAG).d("Request permissions failure");
                                callBack.onFailure(failurePermissions);
                            }

                            if (askNeverAgainPermissions.size() > 0) {
                                Timber.tag(TAG).d("Request permissions failure with ask never again");
                                callBack.onFailureWithAskNeverAgain(askNeverAgainPermissions);
                            }

                            if (failurePermissions.size() == 0 && askNeverAgainPermissions.size() == 0) {
                                Timber.tag(TAG).d("Request permissions success");
                                callBack.onSuccess();
                            }
                        }
                    });
        }
    }

    /**
     * 请求摄像头权限
     */
    public static void launchCamera(FragmentActivity activity, PermissionCallBack callBack) {
        requestPermission(activity, callBack, Manifest.permission.CAMERA);
    }

    /**
     * 请求外部存储的权限
     */
    public static void externalStorage(FragmentActivity activity, PermissionCallBack callBack) {
        requestPermission(activity, callBack, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 请求定位的权限
     */
    public static void accessLocation(FragmentActivity activity, PermissionCallBack callBack) {
        requestPermission(activity, callBack, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * 请求发送短信权限
     */
    public static void sendSms(FragmentActivity activity, PermissionCallBack callBack) {
        requestPermission(activity, callBack, Manifest.permission.SEND_SMS);
    }

    /**
     * 请求打电话权限
     */
    public static void callPhone(FragmentActivity activity, PermissionCallBack callBack) {
        requestPermission(activity, callBack, Manifest.permission.CALL_PHONE);
    }

    /**
     * 请求获取手机状态的权限
     */
    public static void readPhoneState(FragmentActivity activity, PermissionCallBack callBack) {
        requestPermission(activity, callBack, Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 请求获取手机录音的权限
     */
    public static void recordAudio(FragmentActivity activity, PermissionCallBack callBack) {
        requestPermission(activity, callBack, Manifest.permission.RECORD_AUDIO);
    }

    public interface PermissionCallBack {

        /**
         * 权限请求成功
         */
        void onSuccess();

        /**
         * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
         *
         * @param permissions 请求失败的权限名
         */
        void onFailure(List<String> permissions);

        /**
         * 用户拒绝了权限请求并且用户选择了以后不再询问, 权限请求失败, 这时将不能继续请求该权限, 需要提示用户进入设置页面打开该权限
         *
         * @param permissions 请求失败的权限名
         */
        void onFailureWithAskNeverAgain(List<String> permissions);
    }
}

