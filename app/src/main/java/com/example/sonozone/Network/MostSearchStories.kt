package com.example.sonozone


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.http.GET
import com.example.sonozone.Network.RetrofitInstance
import retrofit2.http.Query

interface GetMostSearchStoriesService {
    @GET("api/stories/most/searched")
    suspend fun getMostSearchStories(): StoriesResponse
}

interface SearchStoriesService {
    @GET("api/stories/search")
    suspend fun searchStories(
        @Query("query") query: String
    ): StoriesResponse
}

class MostSearchStoriesStoriesViewModel : ViewModel() {

    val SearchStoriesList = mutableStateOf<List<Story>>(emptyList())

    val Searchloading = mutableStateOf(false)

    fun getTopStories() {

        viewModelScope.launch {

            try {
                Searchloading.value = true

                val response =
                    RetrofitInstance
                        .getMostSearchStoriesService
                        .getMostSearchStories()


                if (response.success) {
                    SearchStoriesList.value=response.data
                    println("Success the Search stories: ${response.data}")
                } else {
                    println("Error: ${response.data}")
                }

            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                Searchloading.value = false
            }
        }
    }

    fun SearchStories(query: String){
        viewModelScope.launch {
            try {
                Searchloading.value = true
                val response = RetrofitInstance.SearchStoriesService.searchStories(query)
                if (response.success) {
                    SearchStoriesList.value = response.data
                    println("Success: ${response.data}")
                } else {
                    println("Error: ${response.data}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                Searchloading.value = false
            }
        }
    }
}