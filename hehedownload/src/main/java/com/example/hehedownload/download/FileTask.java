package com.example.hehedownload.download;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.hehedownload.data.DownloadData;
import com.example.hehedownload.data.Ranges;
import com.example.hehedownload.db.Db;
import com.example.hehedownload.net.HttpManager;
import com.example.hehedownload.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.hehedownload.data.Consts.PROGRESS;
import static com.example.hehedownload.data.Consts.START;
import static com.example.hehedownload.data.Consts.PROGRESS;

public class FileTask implements Runnable {

    private Handler mHandler;
    private String path;
    private String name;
    private Context context;
    private int EACH_TEMP_SIZE = 16;
    private int TEMP_FILE_TOTAL_SIZE;
    private String url;


    @Override
    public void run() {
        try {
            File saveFile = new File(path, name);
            File tempFile = new File(path, name + ".temp");
            DownloadData data = Db.getInstance(context).getData(url);
            if (Utils.isFileExists(saveFile) && Utils.isFileExists(tempFile)
                    && data != null && data.getStatus() != PROGRESS) {
                Response response = OkHttpManager.getInstance().initRequest(url, data.getLastModify());
                if (response != null && response.isSuccessful() && Utils.isNotServerFileChanged(response)) {
                    //服务器端文件没更新,准备下载,回调onstart
                    TEMP_FILE_TOTAL_SIZE = EACH_TEMP_SIZE * data.getChildTaskCount();
                    onStart(data.getTotalLength(), data.getCurrentLength(), "", true);
                } else {
                    //断点下载前准备
                    prepareRangeFile(response);
                }
                saveRangeFile();
            } else {
                Response response = HttpManager.getInstance().initRequest(url);
                if (response != null && response.isSuccessful()) {
                    if (Utils.isSupportRange(response)) {
                        prepareRangeFile(response);
                        saveRangeFile();
                    } else {
                        saveCommonFile(response);
                    }
                }
            }
        } catch (IOException e) {
            onError(e.toString());
        }
    }

    /***
     *进行断点续传的准备工作
     * @param response
     */
    private void prepareRangeFile(Response response) {

    }

    /**
     * 开始断点下载
     */
    private void saveRangeFile() {

        final File saveFile = Utils.createFile(path, name);
        final File tempFile = Utils.createFile(path, name + ".temp");

        final Ranges range = readDownloadRange(tempFile);

        callList = new ArrayList<>();

        Db.getInstance(context).updateProgress(0, 0, PROGRESS, url);

        for (int i = 0; i < childTaskCount; i++) {
            final int tempI = i;
            Call call = HttpManager.getInstance().initRequest(url, range.start[i], range.end[i], new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onError(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    startSaveRangeFile(response, tempI, range, saveFile, tempFile);
                }
            });
            callList.add(call);
        }

        while (tempChildTaskCount < childTaskCount) {
            //由于每个文件采用多个异步操作进行，发起多个异步操作后该线程已经结束，但对应文件并未下载完成，
            //则会出现线程池中同时下载的文件数量超过设定的核心线程数，所以考虑只有当前线程的所有异步任务结束后，
            //才能使结束当前线程。
        }
    }


    public void onStart(long totalLength, long currentLength, String lastModify, boolean isSupportRange){
        Message message = Message.obtain();
        message.what = START;
        Bundle bundle = new Bundle();
        bundle.putInt("totalLengtn", (int) totalLength);
        bundle.putInt("currentLength", (int) currentLength);
        bundle.putString("lastModify", lastModify);
        bundle.putBoolean("isSupportRange", isSupportRange);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    public void onProgress(int length){
        Message message = Message.obtain();
        message.what = PROGRESS;
        message.arg1 = length;
        mHandler.sendMessage(message);
    }
}
