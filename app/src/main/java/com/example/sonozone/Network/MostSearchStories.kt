package com.example.sonozone


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.http.GET
import com.example.sonozone.Network.RetrofitInstance

interface GetMostSearchStoriesService {

    @GET("api/stories/most/searched")
    suspend fun getMostSearchStories(): StoriesResponse
}

class MostSearchStoriesStoriesViewModel : ViewModel() {

    val mostSearchStoriesList = mutableStateOf<List<Story>>(emptyList())

    val mostSearchloading = mutableStateOf(false)

    fun getTopStories() {

        viewModelScope.launch {

            try {
                mostSearchloading.value = true

                val response =
                    RetrofitInstance
                        .getMostSearchStoriesService
                        .getMostSearchStories()


                if (response.success) {
                    mostSearchStoriesList.value=response.data
                    println("Success the most searched: ${response.data}")
                } else {
                    println("Error: ${response.data}")
                }

            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                mostSearchloading.value = false
            }
        }
    }
}