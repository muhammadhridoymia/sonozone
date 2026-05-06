package com.example.sonozone.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sonozone.AuthViewModel

@Composable
fun AuthScreen(navController: NavController) {

    var isLogin by remember { mutableStateOf(true) }

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }


    val BgPage = Color(0xFF0D0D10)
    val BgCard = Color(0xFF18171F)
    val BgInput = Color(0xFF1C1B24)
    val TextPrimary = Color(0xFFF0EEF8)
    val TextSecondary = Color(0xFF9E9CAC)
    val TextMuted = Color(0xFF7A788A)
    val Accent = Color(0xFFA78BFA)

    //api
    val viewModel : AuthViewModel = viewModel()
    val userstate = viewModel.userstate.value
    val authloading = viewModel.authloading.value
    val Message = viewModel.authMessage.value
    val authVerify = viewModel.authVerify.value


    //if authVerify true navigate to VerifyCodeScreen
    LaunchedEffect(authVerify) {
        if (authVerify) {
            navController.navigate("verify")
        }
    }



    fun RegisterApi(){

        if(email == "" || password == ""){
            return
        }
        viewModel.Register(name,email,password)
    }

    fun LoginApi(){
        if(email == "" || password == ""){
            return
        }
        viewModel.Login (email,password)
    }
    LaunchedEffect(userstate) {
        if (userstate != null) {
            navController.navigate("home") {
                popUpTo(0)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPage)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        // Title
        Text(
            text = if (isLogin) "Welcome Back" else "Create Account",
            color = TextPrimary,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = { navController.navigate("verify") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Toke send",
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(6.dp))

        if (Message.isNotEmpty()) {
            Text(
                text = Message,
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(30.dp))

        // Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(BgCard)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            if (!isLogin) {
                InputField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Full Name",
                    textColor = TextPrimary,
                    hintColor = TextMuted,
                    bgColor = BgInput
                )
            }

            InputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email Address",
                textColor = TextPrimary,
                hintColor = TextMuted,
                bgColor = BgInput
            )

            InputField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true,
                textColor = TextPrimary,
                hintColor = TextMuted,
                bgColor = BgInput
            )

            Spacer(Modifier.height(6.dp))

            // Button
            Button(
                enabled = !authloading && password.isNotEmpty() && (email.isNotEmpty() || name.isNotEmpty()),
                onClick = {
                    if (isLogin) {
                        LoginApi()
                    } else {
                        RegisterApi()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (authloading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White)
                }else{
                    Text(
                        text = if (isLogin) "Login" else "Register",
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isLogin) "Don't have an account? " else "Already have an account? ",
                color = TextSecondary
            )

            Text(
                text = if (isLogin) "Register" else "Login",
                color = Accent,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    isLogin = !isLogin
                    name = ""
                    email = ""
                    password = ""
                    viewModel.clearMessage()
                }
            )
        }
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    textColor: Color,
    hintColor: Color,
    bgColor: Color
) {

    var passwordVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = androidx.compose.ui.text.TextStyle(
                color = textColor,
                fontSize = 14.sp
            ),
            modifier = Modifier.weight(1f),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(placeholder, color = hintColor)
                }
                inner()
            }
        )

        if (isPassword) {
            Text(
                text = if (passwordVisible) "Hide" else "Show",
                color = Color(0xFFA78BFA),
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    passwordVisible = !passwordVisible
                }
            )
        }
    }
}