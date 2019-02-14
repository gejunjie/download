package com.example.hehedownload;

import android.content.Context;

import com.example.hehedownload.download.OkDownBuilder;

/**
 * 入口类
 */
public class OkDown {

    public static OkDownBuilder newInstance(Context context){
        return new OkDownBuilder(context);
    }
}
