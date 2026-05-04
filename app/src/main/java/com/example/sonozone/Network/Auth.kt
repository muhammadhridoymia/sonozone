package com.example.sonozone

import android.app.Application
import android.os.Message
import android.util.Log.e
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.sonozone.Network.RetrofitInstance
import com.example.sonozone.Storage.SessionManager
import retrofit2.http.Body
import retrofit2.http.POST


data class AuthResponse(
    val success: Boolean,
    val token : String,
    val user: userInf?,
    val message: String?
)
data class userInf(
    val name: String?
)

data class data(
    val name: String,
    val phone: String?,
    val email: String?,
    val password: String
)


interface RegisterService{
    @POST("api/auth/register")
    suspend fun register(@Body data: data): AuthResponse
}

interface  LoginService{
    @POST("api/auth/login")
    suspend fun login(@Body data: data): AuthResponse
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager: SessionManager? = null
    private val session = SessionManager(application)
    val userstate = mutableStateOf<AuthResponse?>(null)
    val authloading = mutableStateOf(false)
    val authMessage = mutableStateOf("")

    fun Register(name: String, phone: String?, email: String?, password: String) {
        viewModelScope.launch {
            try {
                authloading.value = true
                val response =
                    RetrofitInstance.RegisterService.register(data(name, phone, email, password))
                if (response.success) {
                    userstate.value = response
                    session.saveAuth(response.token, response.user?.name)
                    println("Success: Register ${response}")
                } else {
                    println("Error: Register ${response.user}")
                }
            } catch (e: Exception) {
                e("Error", e.message.toString())
            } finally {
                authloading.value = false
            }
        }
    }

    fun Login(phone: String, email: String?, password: String) {
        viewModelScope.launch {
            try {
                authloading.value = true
                val response =
                    RetrofitInstance.LoginService.login(data("", phone, email, password))
                if (response.success) {
                    userstate.value = response
                    session.saveAuth(response.token, response.user?.name)
                    println("Success: Login ${response}")
                } else {
                    authMessage.value = response.message.toString()
                    println("Error: Login ${response.user}")
                }
            } catch (e: Exception) {
                e("Error", e.message.toString())
            } finally {
                authloading.value = false
            }
        }
    }
}