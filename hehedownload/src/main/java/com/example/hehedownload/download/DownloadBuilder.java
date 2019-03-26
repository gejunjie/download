package com.example.hehedownload.download;

import android.content.Context;

public class DownloadBuilder {

    private String url;
    private String path;           //下载路径
    private String name;           //文件名
    private int childTaskCount;
    //线程数

    private Context context;

    public DownloadBuilder(Context context){
        this.context = context.getApplicationContext();
    }

    public DownloadBuilder url(String url){
        this.url = url;
        return this;
    }

    public DownloadBuilder path(String path){
        this.path = path;
        return this;
    }

    public DownloadBuilder name(String name){
        this.name = name;
        return this;
    }

    public DownloadBuilder childTaskCount(int childTaskCount){
        this.childTaskCount = childTaskCount;
        return this;
    }
    public DownloadManager build() {
        DownloadManager downloadManager = DownloadManager.getInstance(context);

        downloadManager.init(url,path,name,childTaskCount);

        return downloadManager;
    }
}

