package com.example.sonozone

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.sonozone.Network.RetrofitInstance

data class AudioResponse(
    val success: Boolean,
    val data: Audio?
)

data class Audio(
    val audio: AudioStatus?,
    val title: String?,
    val imageUrl: String?,
    val writer: String?,
    val liked: Boolean?
)

data class AudioStatus(
    val bangla: Urls?,
    val english: Urls?,
    val arabic: Urls?
)
data class Urls(
    val url: String?,
    val length: Int?
)

interface AudioService {
    @GET("api/stories/audio/{id}")
    suspend fun getAudio(
        @Path("id") id: String
    ): AudioResponse
}

class AudioViewModel : ViewModel() {

    val selectedAudio = mutableStateOf<Audio?>(null)
    val isAudioLoading = mutableStateOf(false)

    fun fetchAudio(id: String) {
        viewModelScope.launch {
            try {
                isAudioLoading.value = true

                val response = RetrofitInstance
                    .getAudioServices
                    .getAudio(id)

                if (response.success && response.data != null) {
                    selectedAudio.value = response.data
                    println("Success API Response: ${response.data}")
                } else {
                    println("API Error: Success flag was false or data was null")
                }

            } catch (e: Exception) {
                println("Network/Parsing Error: ${e.message}")
            } finally {
                isAudioLoading.value = false
            }
        }
    }
}