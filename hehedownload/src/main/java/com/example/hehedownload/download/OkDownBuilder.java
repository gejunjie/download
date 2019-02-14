package com.example.hehedownload.download;

import android.content.Context;

public class OkDownBuilder {

    private String url;
    private String path;           //下载路径
    private String name;           //文件名
    private String childTaskCount; //线程数

    private Context context;

    public OkDownBuilder(Context context){
        this.context = context.getApplicationContext();
    }

    public OkDownBuilder url(String url){
        this.url = url;
        return this;
    }

    public OkDownBuilder path(String path){
        this.path = path;
        return this;
    }

    public OkDownBuilder name(String name){
        this.name = name;
        return this;
    }

    public OkDownBuilder childTaskCount(String childTaskCount){
        this.childTaskCount = childTaskCount;
        return this;
    }
}

