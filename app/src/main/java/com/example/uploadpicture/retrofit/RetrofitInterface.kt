package com.example.uploadpicture.retrofit

import com.example.uploadpicture.api.ImageClass
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitInterface {
    @Multipart
    @POST("upload")
    fun upLoadImage(
        @Part Image: MultipartBody.Part
    ): Call<ImageClass>
}