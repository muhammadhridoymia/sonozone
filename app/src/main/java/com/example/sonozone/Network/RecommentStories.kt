package com.example.sonozone


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.sonozone.Network.RetrofitInstance


interface RecommentStoriesService {

    @GET("api/stories/recommend/{id}")
    suspend fun RecommentStories(
        @Path("id") id: String
    ): StoriesResponse
}

class RecommentStoriesViewModel : ViewModel() {

    val RecommentStoriesList = mutableStateOf<List<Story>>(emptyList())
    val Recommentloading = mutableStateOf(false)

    fun getRecommentStories(id: String) {

        viewModelScope.launch {

            try {
                Recommentloading.value = true

                val response =
                    RetrofitInstance
                        .getRecommentStoriesService
                        .RecommentStories(id)


                if (response.success) {
                    RecommentStoriesList.value=response.data
                    println("Success the RecommentStories: ${response.data}")
                } else {
                    println("Error : ${response.data}")
                }

            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                Recommentloading.value = false
            }
        }
    }
}