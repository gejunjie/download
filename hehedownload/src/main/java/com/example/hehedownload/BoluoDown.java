package com.example.hehedownload;

import android.content.Context;

import com.example.hehedownload.download.DownloadBuilder;

/**
 * 入口类
 */
public class BoluoDown {

    public static DownloadBuilder init(Context context){
        return new DownloadBuilder(context);
    }
}
