package com.example.sonozone.Network


import com.example.sonozone.GetTopStoriesService
import com.example.sonozone.AudioService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.0.108:5000/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getTopStoriesService: GetTopStoriesService by lazy {
        retrofit.create(GetTopStoriesService::class.java)
    }

    val getAudioServices: AudioService by lazy {
        retrofit.create(AudioService::class.java)
    }

}
