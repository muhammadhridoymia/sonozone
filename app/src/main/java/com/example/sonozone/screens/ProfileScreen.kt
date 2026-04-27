package com.example.sonozone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {

    // Sample user data
    var balance by remember { mutableStateOf(50) }
    val username = "Sarah Johnson"
    val email = "sarah.johnson@example.com"
    val profileImage = "https://picsum.photos/200/200"
    val savedStories = 12

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Profile Image
        AsyncImage(
            model = profileImage,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Username
        Text(
            text = username,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        // Email
        Text(
            text = email,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Balance Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Balance",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = "$${balance}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { balance += 10 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF6200EE)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add Balance +$10")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Saved Stories Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Saved Stories",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$savedStories",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Optional logout or settings button
        OutlinedButton(
            onClick = { /* Logout action */ },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Logout")
        }
    }
}