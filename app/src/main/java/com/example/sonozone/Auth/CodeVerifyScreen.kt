package com.example.sonozone.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sonozone.AuthViewModel

@Composable
fun CodeVerifyScreen(navController: NavController) {

    var Code by remember { mutableStateOf("") }

    // Modern Dark Theme Colors with better proportions
    val BgPage = Color(0xFF0A0A0F)      // Deeper dark background
    val SurfaceColor = Color(0xFF282630)  // Input field surface
    val TextPrimary = Color(0xFFFFFFFF)   // Pure white for primary text
    val TextSecondary = Color(0xFFA9A8B3) // Muted gray for secondary text
    val AccentColor = Color(0xFF7C3AED)   // Vibrant purple accent
    val ErrorColor = Color(0xFFEF4444)    // Red for errors

    // API state
    val viewModel: AuthViewModel = viewModel()
    val authloading = viewModel.authloading.value
    val Message = viewModel.authMessage.value

    LaunchedEffect(Unit) {
        viewModel.VerifyCode( Code.toInt())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPage)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Header Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Verification Code",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Please enter the 6-digit code sent to your email",
                color = TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(Modifier.height(32.dp))

        // Error Message
        if (Message.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ErrorColor.copy(alpha = 0.1f))
                    .padding(12.dp)
            ) {
                Text(
                    text = Message,
                    color = ErrorColor,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        // Card Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Code Input Label
            Text(
                text = "Enter Code",
                color = Color.Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            // Verification Code Input
            BasicTextField(
                value = Code,
                onValueChange = {
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        Code = it
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = VisualTransformation.None,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 8.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceColor)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (Code.isEmpty()) {
                            Text(
                                text = "000000",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 8.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            // Code hint
            Text(
                text = "6-digit verification code",
                color = Color.Black,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(Modifier.height(8.dp))

            // Submit Button
            Button(
                enabled = !authloading && Code.length == 6,
                onClick = {
                    // Add verification logi
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentColor,
                    disabledContainerColor = AccentColor.copy(alpha = 0.3f)
                )
            ) {
                if (authloading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Verify Account",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Resend Code Option
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Didn't receive code? ",
                    color = Color.Black,
                    fontSize = 13.sp
                )
                Text(
                    text = "Resend",
                    color = AccentColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        // Add resend code logic

                    }
                )
            }
        }

        // Back to Login Link
        Spacer(Modifier.height(16.dp))

        Text(
            text = "← Back to Login",
            color = TextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.clickable {
                navController.popBackStack()
            }
        )
    }
}
