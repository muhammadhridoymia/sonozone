package com.example.sonozone


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.sonozone.Network.RetrofitInstance

data class StoriesResponse(
    val success: Boolean,
    val data: List<Story>
)

data class Story(
    val _id: String?,
    val title: String?,
    val imageUrl: String?,
    val duration: Int?,
    val status: Status?,
    val writer: String?
)

data class Status(
    val views: Int?,
    val likes: Int?,
    val comments: Int?
)

interface GetTopStoriesService {

    @GET("api/stories/all/tops/{period}")
    suspend fun getTopStories(
        @Path("period") period: String
    ): StoriesResponse
}

class TopStoriesViewModel : ViewModel() {

    val allTimeList = mutableStateOf<List<Story>>(emptyList())
    val weekList = mutableStateOf<List<Story>>(emptyList())
    val monthList = mutableStateOf<List<Story>>(emptyList())
    val yearList = mutableStateOf<List<Story>>(emptyList())
    val loading = mutableStateOf(false)

    fun getTopStories(period: String) {

        viewModelScope.launch {

            try {
                loading.value = true

                val response =
                    RetrofitInstance
                        .getTopStoriesService
                        .getTopStories(period)

                if (response.success) {
                    if (period=="allTime"){
                        allTimeList.value = response.data
                    }
                    if(period == "week"){
                        weekList.value = response.data
                    }
                    if(period == "month"){
                        monthList.value = response.data
                    }
                    if(period == "year"){
                        yearList.value = response.data

                    }
                    println("Success: ${response.data}")
                } else {
                    println("Error here hridoy: ${response.data}")
                }

            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                loading.value = false
            }
        }
    }
}