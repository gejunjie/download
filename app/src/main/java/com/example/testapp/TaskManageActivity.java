package com.example.testapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hehedownload.BoluoDown;
import com.example.hehedownload.callback.DownloadCallback;
import com.example.hehedownload.download.DownloadManager;
import com.example.hehedownload.utils.Utils;

import java.io.File;

/**
 * Created by Benboerboluo on 2019/3/22.
 */
public class TaskManageActivity extends AppCompatActivity {
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.INTERNET};
    private static final int MY_PERMISSIONS_REQUEST = 100;


    /**
     * http://1.198.5.23/imtt.dd.qq.com/16891/B8723A0DB2F2702C04D801D9FD19822C.apk //阴阳师
     * http://1.82.215.170/imtt.dd.qq.com/16891/85B6221DE84C466310575D9FBCA453A8.apk  //天天酷跑
     * http://1.198.5.22/imtt.dd.qq.com/16891/8EEC7D8996760973B5CEA15ECA1700E3.apk  //消消乐
     */

    private TextView mTip;
    private TextView mProgress;
    private TextView mPause;
    private TextView mResume;
    private TextView mCancel;
    private TextView mRestart;
    private ProgressBar progressBar;

    private Context mContext;

    private String url;

    private DownloadManager downloadManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //动态权限申请
        checkAppPermission();

        mContext = this;

        mTip = (TextView) findViewById(R.id.tip);
        mProgress = (TextView) findViewById(R.id.progress);
        mPause = (TextView) findViewById(R.id.pause);
        mResume = (TextView) findViewById(R.id.resume);
        mCancel = (TextView) findViewById(R.id.cancel);
        mRestart = (TextView) findViewById(R.id.restart);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()

        final String name = "消消乐";
        url = "http://1.198.5.22/imtt.dd.qq.com/16891/8EEC7D8996760973B5CEA15ECA1700E3.apk";



        downloadManger = BoluoDown.init(mContext)
                .url(url)
                .path(Environment.getExternalStorageDirectory() + "/OkDown/")
                .name(name + ".mp4")
                .childTaskCount(3)
                .build()
                .start(new DownloadCallback() {

                    @Override
                    public void onStart(long currentSize, long totalSize, float progress) {
                        mTip.setText(name + "：准备下载中...");
                        progressBar.setProgress((int) progress);
                        mProgress.setText(Utils.formatSize(currentSize) + " / " + Utils.formatSize(totalSize) + "--------" + progress + "%");
                    }

                    @Override
                    public void onProgress(long currentSize, long totalSize, float progress) {
                        mTip.setText(name + "：下载中...");
                        progressBar.setProgress((int) progress);
                        mProgress.setText(Utils.formatSize(currentSize) + " / " + Utils.formatSize(totalSize) + "--------" + progress + "%");
                    }

                    @Override
                    public void onPause() {
                        mTip.setText(name + "：暂停中...");
                    }

                    @Override
                    public void onCancel() {
                        mTip.setText(name + "：已取消...");
                    }

                    @Override
                    public void onFinish(File file) {}

                    @Override
                    public void onWait() {}

                    @Override
                    public void onError(String error) {
                        mTip.setText(name + "：下载出错...");
                    }
                });

     }



    //     ========================================================================================================================================
//    权限申请

    private void checkAppPermission() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            if ( ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
            }
            if ( ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST);
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else
                        finish();
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showDialogTipUserGoToAppSettting() {
    }
}
