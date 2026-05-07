package com.example.sonozone

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sonozone.Network.RetrofitInstance
import com.example.sonozone.Storage.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// DATA CLASSES

data class AuthResponse(
    val success: Boolean?,
    val token: String?,
    val user: userInf?,
    val verify: Boolean?,
    val message: String?
)

data class userInf(
    val name: String?
)

data class data(
    val name: String?,
    val email: String?,
    val password: String
)

// API SERVICES

interface RegisterService {

    @POST("api/auth/register")
    suspend fun register(
        @Body data: data
    ): Response<AuthResponse>
}

interface LoginService {

    @POST("api/auth/login")
    suspend fun login(
        @Body data: data
    ): Response<AuthResponse>
}

interface VerifyCodeService {

    @GET("api/auth/verify/{code}")
    suspend fun verifyCode(
        @Path("code") code: String
    ): Response<AuthResponse>
}

// VIEWMODEL

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val session = SessionManager(application)

    val userstate = mutableStateOf<AuthResponse?>(null)

    val authloading = mutableStateOf(false)

    val authMessage = mutableStateOf("")

    val authVerify = mutableStateOf(false)

    fun clearMessage() {
        authMessage.value = ""
    }

    // REGISTER

    fun Register(
        name: String,
        email: String?,
        password: String
    ) {

        viewModelScope.launch {

            try {

                authloading.value = true

                val response =
                    RetrofitInstance.RegisterService.register(
                        data(name, email, password)
                    )

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body?.verify == true) {

                        authVerify.value = true

                        authMessage.value =
                            body.message ?: "Verification email sent"

                        Log.d("Register", "Success: ${body.message}")

                    } else {

                        authMessage.value =
                            body?.message ?: "Registration failed"
                    }

                } else {

                    authMessage.value = handleError(response.code())
                }

            } catch (e: Exception) {

                Log.e("Register Error", e.message.toString())

                authMessage.value =
                    e.message ?: "Something went wrong"

            } finally {

                authloading.value = false
            }
        }
    }

    // LOGIN

    fun Login(
        email: String?,
        password: String
    ) {

        viewModelScope.launch {

            try {

                authloading.value = true

                val response =
                    RetrofitInstance.LoginService.login(
                        data("", email, password)
                    )

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body?.success == true) {

                        userstate.value = body

                        session.saveAuth(
                            body.token ?: "",
                            body.user?.name
                        )

                        authMessage.value =
                            body.message ?: "Login successful"

                        Log.d("Login", "Success")

                    } else if (body?.verify == true) {

                        authVerify.value = true

                        authMessage.value =
                            body.message ?: "Please verify your account"

                    } else {

                        authMessage.value =
                            body?.message ?: "Login failed"
                    }

                } else {

                    authMessage.value = handleError(response.code())
                }

            } catch (e: Exception) {

                Log.e("Login Error", e.message.toString())

                authMessage.value =
                    e.message ?: "Something went wrong"

            } finally {

                authloading.value = false
            }
        }
    }

    // VERIFY CODE

    fun VerifyCode(code: String) {

        viewModelScope.launch {

            try {

                authloading.value = true

                val response =
                    RetrofitInstance.VerifyCodeService.verifyCode(code)

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body?.success == true) {

                        userstate.value = body

                        session.saveAuth(
                            body.token ?: "",
                            body.user?.name
                        )

                        authMessage.value =
                            body.message ?: "Verification successful"

                        Log.d("Verify", "Success")

                    } else {

                        authMessage.value =
                            body?.message ?: "Invalid verification code"
                    }

                } else {

                    authMessage.value = handleError(response.code())
                }

            } catch (e: Exception) {

                Log.e("Verify Error", e.message.toString())

                authMessage.value =
                    e.message ?: "Something went wrong"

            } finally {

                authloading.value = false
            }
        }
    }

    // ERROR HANDLER

    private fun handleError(code: Int): String {

        return when (code) {

            400 -> "Bad Request"

            401 -> "Wrong Password"

            403 -> "Forbidden"

            404 -> "Not Found"

            405 -> "User with this email already exists please login"

            500 -> "Server Error"

            else -> "Something went wrong"
        }
    }
}