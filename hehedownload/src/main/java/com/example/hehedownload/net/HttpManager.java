package com.example.hehedownload.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

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

    /**
     * 下载文件时判断是否被更改
     * @param url
     * @param lastModify
     * @return
     * @throws IOException
     */
    public Response initRequest(String url, String lastModify) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Range", "bytes=0-")
                .header("If-Range", lastModify)
                .build();
        return builder.build().newCall(request).execute();
    }

    /**
     * 初始化证书
     * @param certificates
     */
    public void setCertificates(InputStream... certificates){
        try{
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates){
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null){
                        certificate.close();
                    }
                }catch (IOException e){}
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory());
        }catch (Exception e){}
    }
}
