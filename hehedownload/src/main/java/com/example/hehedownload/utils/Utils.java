package com.example.hehedownload.utils;

import java.io.File;
import java.util.StringTokenizer;

import okhttp3.Response;

public class Utils {

    public static float getPercentage(int currentLength, int totalLength){
        if (currentLength > totalLength){
            return 0;
        }
        return ((int)(currentLength * 10000.0 / totalLength)) *1.0f /100;
    }

    /**
     * 服务器文件是否已更改
     * @param response
     * @return
     */
    public static boolean isNotServerFileChanged(Response response){
        return response.code() == 206;
    }

    public static String getLastModify(Response response){
        return response.headers().get("Last-Modified");
    }

    public static boolean isFileExits(File file){
        return file.exists();
    }
}
