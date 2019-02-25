package com.example.hehedownload.download;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
    //CPU核心数
    private int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private int CORE_POOL_SIZE = 3;

    private int MAX_POOL_SIZE = 20;

    private long KEEP_ALIVE = 10L;

    private ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    private ThreadFactory mThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "download_task#" + mCount.getAndIncrement());
        }
    };

    private ThreadPool(){

    }

    public ThreadPool getInstance(){
        return ThreadPoolHolder.instance;
    }

    public static class ThreadPoolHolder{
        public static ThreadPool instance = new ThreadPool();
    }

    public void setCorePoolSize(int corePoolSize){
        if (corePoolSize == 0){
            return;
        }
        CORE_POOL_SIZE = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize){
        if (maxPoolSize == 0){
            return;
        }
        MAX_POOL_SIZE =maxPoolSize;
    }

    public int getCorePoolSize() {
        return CORE_POOL_SIZE;
    }

    public int getMaxPoolSize() {
        return MAX_POOL_SIZE;
    }

    public ThreadPoolExecutor getExecutor(){
        if (THREAD_POOL_EXECUTOR == null){
            return new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,
                    KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(),
                    mThreadFactory);
        }
        return THREAD_POOL_EXECUTOR;
    }
}
