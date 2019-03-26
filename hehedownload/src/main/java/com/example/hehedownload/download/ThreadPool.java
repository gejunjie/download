package com.example.hehedownload.download;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ThreadPool {
    //CPU核心数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    //最大线程数
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 +1;

    private long KEEP_ALIVE = 10L;

    private ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    private ThreadFactory mThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "download_task#" + mCount.getAndIncrement());
        }
    };

    private ThreadPool(){

    }

    public static ThreadPool getInstance(){
        return ThreadPoolHolder.instance;
    }

    private static class ThreadPoolHolder{
        private static final ThreadPool instance = new ThreadPool();
    }

    public ThreadPoolExecutor getExecutor(){
        if (THREAD_POOL_EXECUTOR == null){
            return new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,
                    KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(),
                    mThreadFactory);
        }
        return THREAD_POOL_EXECUTOR;
    }
    //    public void setCorePoolSize(int corePoolSize){
//        if (corePoolSize == 0){
//            return;
//        }
//        CORE_POOL_SIZE = corePoolSize;
//    }
//
//    public void setMaxPoolSize(int maxPoolSize){
//        if (maxPoolSize == 0){
//            return;
//        }
//        MAX_POOL_SIZE =maxPoolSize;
//    }

    public int getCorePoolSize() {
        return CORE_POOL_SIZE;
    }
//
//    public int getMaxPoolSize() {
//        return MAX_POOL_SIZE;
//    }

}
