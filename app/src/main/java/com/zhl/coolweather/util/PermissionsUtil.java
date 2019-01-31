package com.zhl.coolweather.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.litepal.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxd_dary on 2018/11/9.
 *
 * 权限工具使用注意：调用对象必须实现onRequestPermissionsResult方法并调用本类的onRequestPermissionsResult方法
 */

public class PermissionsUtil {

    private static final PermissionsUtil permissionsUtil = new PermissionsUtil();
    private IPermissionsResult mPermissionsResult;
    private int mRequestCode = 100;//权限请求码
    public static boolean showSystemSetting = false;
    private boolean isFirst = true;

    private PermissionsUtil() {

    }

    public static PermissionsUtil getInstance() {

        /*if (permissionsUtil == null) {
            synchronized (PermissionsUtil.class) {
                if (permissionsUtil == null) {
                    permissionsUtil = new PermissionsUtil();
                }
            }
        }*/
        return permissionsUtil;

    }

    public void checkPermissions(Activity context, String[] permissions, IPermissionsResult permissionsResult) {
        mPermissionsResult = permissionsResult;

        if (Build.VERSION.SDK_INT < 23) {//6.0才用动态权限
            permissionsResult.passPermissons();
        } else {
            //创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
            List<String> mPermissionList = new ArrayList<>();
            //逐个判断你要的权限是否已经通过
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);//添加还未授予的权限
                    if (!context.shouldShowRequestPermissionRationale(permissions[i])) {
                        //当第一次请求调用时和选择了不再询问后再次请求，会进入该方法
                        if (isFirst){
                            //第一次请求不做处理
                            isFirst =false;
                        }else {
                            //获取手机品牌
                            String brand = Build.BRAND;
                            LogUtil.d("+++++++","已默认拒绝权限，请手动开启权限！手机品牌："+brand);
                            if (!"Xiaomi".equals(brand)){
                                //选择不再询问后，小米手机手动设置权限无效，所以小米不要跳到应用设置页面
                                showSystemSetting =true;
                            }
                        }
                    }
                }
            }

            //申请权限
            if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
                ActivityCompat.requestPermissions(context, permissions, mRequestCode);
            } else {
                //说明权限都已经通过，可以做你想做的事情去
                permissionsResult.passPermissons();
            }
        }
    }

    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
    public void onRequestPermissionsResult(Activity context, int requestCode, String[] permissions,
                                           int[] grantResults) {
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                if (showSystemSetting) {
                    showSystemPermissionsSettingDialog(context);//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                } else {
                    mPermissionsResult.forbitPermissons();
                }
            } else {
                //全部权限通过，可以进行下一步操作。。。
                mPermissionsResult.passPermissons();
            }
        }
    }


    /**
     * 不再提示权限时的展示对话框
     */
    AlertDialog mPermissionDialog;

    private void showSystemPermissionsSettingDialog(final Activity context) {
        final String mPackName = context.getPackageName();
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(context)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            context.startActivity(intent);
                            context.finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                            //mContext.finish();
                            mPermissionsResult.forbitPermissons();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    //关闭对话框
    private void cancelPermissionDialog() {
        if (mPermissionDialog != null) {
            mPermissionDialog.cancel();
            mPermissionDialog = null;
        }
    }

    public interface IPermissionsResult {
        void passPermissons();
        void forbitPermissons();
    }

}
