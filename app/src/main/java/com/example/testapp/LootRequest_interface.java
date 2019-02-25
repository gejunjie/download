package com.example.testapp;



import io.reactivex.Observable;
import retrofit2.http.GET;

public interface LootRequest_interface {
    @GET("ajax.php?a=fy&f=auto&=auto$w=hi")
    Observable<Person> getCall();
}
