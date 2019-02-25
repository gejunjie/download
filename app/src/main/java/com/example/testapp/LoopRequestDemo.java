package com.example.testapp;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoopRequestDemo {

    public void loop(){
        Observable.interval(2,1, TimeUnit.SECONDS)

                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://fy.iciba.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

                        LootRequest_interface request = retrofit.create(LootRequest_interface.class);
                        Observable<Person> observable = request.getCall();
                        observable.subscribeOn(Schedulers.io())//切换到io线程
                            .subscribeOn(Schedulers.newThread())
                                .subscribe(new Observer<Person>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Person person) {
                                        //接收服务器返回结果
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                });
    }
}
