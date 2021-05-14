package com.example.uploadpicture.retrofit

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

object RetrofitClient {
//    private var ourInstance: Retrofit? = null
    private const val BASE_URL: String = "https://darot-image-upload-service.herokuapp.com/api/v1/"

    fun getImage(): RetrofitInterface{
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return Retrofit.Builder()
                    .client(OkHttpClient.Builder().addInterceptor(logging).build())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                     .create(RetrofitInterface::class.java)
    }

}