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
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


data class AuthResponse(
    val success: Boolean?,
    val token : String?,
    val user: userInf?,
    val verify: Boolean?,
    val message: String?
)
data class userInf(
    val name: String?
)

data class data(
    val name: String,
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
interface VerifyCodeService {
    @GET("api/auth/verify/{code}")
    suspend fun verifyCode(
        @Path("code") code: String
    ): AuthResponse
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager: SessionManager? = null
    fun clearMessage() {
        authMessage.value = ""
    }
    private val session = SessionManager(application)
    val userstate = mutableStateOf<AuthResponse?>(null)
    val authloading = mutableStateOf(false)
    val authMessage = mutableStateOf("")
    val authVerify = mutableStateOf(false)

    fun Register(name: String, email: String?, password: String) {
        viewModelScope.launch {
            try {
                authloading.value = true
                val response = RetrofitInstance.RegisterService.register(data(name, email, password))

                if (response.verify==true) {
                    authVerify.value=true
                    println( "Regiser data: ${response.verify}")
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

    fun Login( email: String?, password: String) {
        viewModelScope.launch {
            try {
                authloading.value = true
                val response =
                    RetrofitInstance.LoginService.login(data("",  email, password))
                 if (response.success ==true){
                    userstate.value = response
                    session.saveAuth(response.token?:"", response.user?.name)
                    println("Success: Login ${response}")
                } else if (response.verify == true){
                    authVerify.value=true
                 }
                 else {
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
    fun VerifyCode(code: String){
        viewModelScope.launch {
            try {
                authloading.value = true
                val response = RetrofitInstance.VerifyCodeService.verifyCode(code)
                if (response.success==true) {
                    userstate.value = response
                    session.saveAuth( response.token?:"", response.user?.name)
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