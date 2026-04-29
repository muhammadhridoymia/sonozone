package com.example.sonozone.Network


import com.example.sonozone.GetTopStoriesService


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://172.172.9.133:5000/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getTopStoriesService: GetTopStoriesService by lazy {
        retrofit.create(GetTopStoriesService::class.java)
    }
}
