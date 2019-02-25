package com.example.testapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface retrofit_interface {
    @GET("")
    Call<Object> getCall();
}
