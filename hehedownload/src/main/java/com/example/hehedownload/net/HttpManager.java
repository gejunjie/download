package com.example.hehedownload.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpManager {

    private final OkHttpClient.Builder builder;

    private HttpManager(){
        builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS);
    }

    public static HttpManager getInstance(){
        return HttpManagerHolder.instance;
    }

    private static class HttpManagerHolder{
        private static final HttpManager instance = new HttpManager();
    }

    /**
     * 异步下载
     * @param url
     * @param start       开始下载的字节
     * @param end         结束下载的字节
     * @param callback
     * @return
     */
    public Call initRequest(String url, long start, long end, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .header("Range","bytes=" + start + "-" +end)
                .build();
        Call call = builder.build().newCall(request);
        call.enqueue(callback);
        return call;
    }

    /**
     * 同步下载
     * @param url
     * @return
     * @throws IOException
     */
    public Response initRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Range", "bytes=0-")
                .build();
        return builder.build().newCall(request).execute();
    }
}
