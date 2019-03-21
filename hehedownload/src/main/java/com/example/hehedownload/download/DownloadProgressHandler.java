package com.example.hehedownload.download;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.hehedownload.callback.DownloadCallback;
import com.example.hehedownload.data.DownloadData;
import com.example.hehedownload.db.Db;
import com.example.hehedownload.utils.Utils;

import static com.example.hehedownload.data.Consts.CANCEL;
import static com.example.hehedownload.data.Consts.ERROR;
import static com.example.hehedownload.data.Consts.FINISH;
import static com.example.hehedownload.data.Consts.NONE;
import static com.example.hehedownload.data.Consts.PAUSE;
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

    private FileTask fileTask;

    private int mCurrentState = NONE;

    //是否支持断点续传
    private boolean isSupportRange;

    //重新开始下载需要先进行取消操作
    private boolean isNeedRestart;

    //记录已经下载的大小
    private int currentLength = 0;
    //记录文件总大小
    private int totalLength = 0;
    //记录已经暂停或取消的线程数
    private int tempChildTaskCount = 0;

    private long lastProgressTime;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int lastState = mCurrentState;
            mCurrentState = msg.what;
            mDownloadData.setStatus(mCurrentState);

            switch (mCurrentState){
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

    public DownloadProgressHandler(Context context, DownloadData downloadData, DownloadCallback downloadCallback) {
        this.mContext = context;
        this.mCallbck = downloadCallback;

        this.url = downloadData.getUrl();
        this.path = downloadData.getPath();
        this.name = downloadData.getName();
        this.childTaskCount = downloadData.getChildTaskCount();

        DownloadData dbData = Db.getInstance(context).getData(url);
        this.mDownloadData = dbData == null ? downloadData : dbData;
    }

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

    public int getCurrentState(){
        return mCurrentState;
    }

    public DownloadData getDownloadData(){
        return mDownloadData;
    }

    public void setFileTask(FileTask task){
        this.fileTask = task;
    }

    /**
     * 下载中退出时保存数据、释放资源
     */
    public void destroy() {
        if (mCurrentState == CANCEL || mCurrentState == PAUSE) {
            return;
        }
        fileTask.destroy();
    }

    /**
     * 暂停（正在下载才可以暂停）
     * 如果文件不支持断点续传则不能进行暂停操作
     */
    public void pause() {
        if (mCurrentState == PROGRESS) {
            fileTask.pause();
        }
    }

    /**
     * 取消（已经被取消、下载结束则不可取消）
     */
    public void cancel(boolean isNeedRestart) {
        this.isNeedRestart = isNeedRestart;
        if (mCurrentState == PROGRESS) {
            fileTask.cancel();
        } else if (mCurrentState == PAUSE || mCurrentState == ERROR) {
            handler.sendEmptyMessage(CANCEL);
        }
    }
}
