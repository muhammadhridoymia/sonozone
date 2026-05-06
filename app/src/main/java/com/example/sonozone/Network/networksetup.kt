package com.example.sonozone.Network


import com.example.sonozone.GetTopStoriesService
import com.example.sonozone.AudioService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.sonozone.GetMostSearchStoriesService
import com.example.sonozone.RecommentStoriesService

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

    val getMostSearchStoriesService: GetMostSearchStoriesService by lazy {
        retrofit.create(GetMostSearchStoriesService::class.java)
    }

    val getRecommentStoriesService: RecommentStoriesService by lazy {
        retrofit.create(RecommentStoriesService::class.java)
    }

    val SearchStoriesService: com.example.sonozone.SearchStoriesService by lazy {
        retrofit.create(com.example.sonozone.SearchStoriesService::class.java)
    }

    val RegisterService: com.example.sonozone.RegisterService by lazy {
        retrofit.create(com.example.sonozone.RegisterService::class.java)
    }

    val LoginService: com.example.sonozone.LoginService by lazy {
        retrofit.create(com.example.sonozone.LoginService::class.java)
    }

    val VerifyCodeService: com.example.sonozone.VerifyCodeService by lazy {
        retrofit.create(com.example.sonozone.VerifyCodeService::class.java)
    }



}
