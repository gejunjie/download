package com.example.hehedownload.download;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.hehedownload.callback.DownloadCallback;
import com.example.hehedownload.data.DownloadData;
import com.example.hehedownload.db.Db;
import com.example.hehedownload.utils.Utils;

import static com.example.hehedownload.data.Consts.FINISH;
import static com.example.hehedownload.data.Consts.NONE;
import static com.example.hehedownload.data.Consts.PROGRESS;
import static com.example.hehedownload.data.Consts.START;

public class DownloadProgressHandler {

    private String url;
    private String path;
    private String name;

    private int childTaskCount;
    private Context mContext;
    private DownloadCallback mCallbck;
    private DownloadData mDownloadData;
    private int mCurrentStatus = NONE;
    private FileTask mFileTask;
    private boolean isSupportRange;
    private int currentLength = 0;
    private int totalLength = 0;
    private int tempChildTaskCount = 0;

    private long lastProgressTime = 0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int lastState = mCurrentStatus;
            mCurrentStatus = msg.what;
            mDownloadData.setStatus(mCurrentStatus);

            switch (mCurrentStatus){
                case START :
                    Bundle data = msg.getData();
                    totalLength = data.getInt("totalLength");
                    currentLength = data.getInt("currentLength");
                    String lastModify = data.getString("lastModify");
                    isSupportRange = data.getBoolean("isSupportRange");

                    if (!isSupportRange) {
                        childTaskCount = 1;
                    } else if (currentLength == 0) {
                        Db.getInstance(mContext).insertData(new DownloadData(url, path, childTaskCount, name, currentLength, totalLength, lastModify, System.currentTimeMillis()));
                    }
                    if (mCallbck != null) {
                        mCallbck.onStart(currentLength, totalLength, Utils.getPercentage(currentLength, totalLength));
                    }
                    break;
                case PROGRESS:
                    synchronized (this){
                        currentLength += msg.arg1;
                        //计算百分比
                        mDownloadData.setPercentage(Utils.getPercentage(currentLength, totalLength));

                        if (mCallbck != null ||
                                System.currentTimeMillis() - lastProgressTime >= 20 ||
                                currentLength == totalLength){
                            mCallbck.onProgress(currentLength, totalLength, Utils.getPercentage(currentLength, totalLength));
                            lastProgressTime = System.currentTimeMillis();
                        }

                        if (currentLength == totalLength){
                            sendEmptyMessage(FINISH);
                        }
                    }
                    break;
            }
        }
    };

    public DownloadProgressHandler(String url, String path, String name, int childTaskCount, Context mContext, DownloadData mDownloadData) {
        this.url = url;
        this.path = path;
        this.name = name;
        this.childTaskCount = childTaskCount;
        this.mContext = mContext;
        this.mDownloadData = mDownloadData;
        DownloadData data = Db.getInstance(mContext).getData(url);
        mDownloadData = data == null ? mDownloadData : data;
    }

    public Handler getHandler(){
        return handler;
    }

    public int getCurrentStatus(){
        return mCurrentStatus;
    }

    public DownloadData getDownloadData(){
        return mDownloadData;
    }

    public void setFileTask(FileTask task){
        this.mFileTask = task;
    }
}
