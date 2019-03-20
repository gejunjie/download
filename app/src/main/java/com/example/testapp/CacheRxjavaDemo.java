package com.example.testapp;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class CacheRxjavaDemo {
    String memoryCache = null;
    String diskCache = "从磁盘缓存获取数据";
    //设置第一个observable,检查内存缓存是否有该数据的缓存
    Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
            if (memoryCache != null){
                emitter.onNext(memoryCache);
            }else {
                emitter.onComplete();
            }
        }
    });

    //设置第二个observable,检查磁盘缓存是否有该数据的缓存
    Observable<String> disk = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
            if(diskCache != null){
                emitter.onNext(diskCache);
            }else {
                emitter.onComplete();
            }
        }
    });

    //设置第三个observable，通过网络获取数据
    Observable<String> network = Observable.just("从网络下载");

    public void doCache(){
        Observable.concat(memory,disk,network)
                .firstElement()//发送第一个有效事件
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                });
    }

}