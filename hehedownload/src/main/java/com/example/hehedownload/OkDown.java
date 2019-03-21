package com.example.hehedownload;

import android.content.Context;

import com.example.hehedownload.download.DownloadBuilder;

/**
 * 入口类
 */
public class OkDown {

    public static DownloadBuilder newInstance(Context context){
        return new DownloadBuilder(context);
    }
}
