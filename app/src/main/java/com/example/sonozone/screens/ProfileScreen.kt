package com.example.sonozone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sonozone.ui.components.LogoutDialog
import com.example.sonozone.Auth.AuthScreen
import com.example.sonozone.Storage.SessionManager

private val BgPage = Color(0xFF0D0D10)

@Composable
fun ProfileScreen(navController: NavController, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var token by remember { mutableStateOf(sessionManager.getToken()) }
    var name by remember { mutableStateOf(sessionManager.getName()) }


    if (token == null) {
        AuthScreen(navController)
        return
    }



    var showLogoutDialog by remember { mutableStateOf(false) }
    var balance by remember { mutableStateOf(50) }

    val username = "Sarah Johnson"
    val email = "sarah.johnson@example.com"
    val profileImage = "https://picsum.photos/200/200"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgPage)
            .padding(16.dp)
            .verticalScroll( rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (showLogoutDialog) {
            LogoutDialog(
                showDialog = showLogoutDialog,
                onDismiss = { showLogoutDialog = false },
                onLogout = {
                    sessionManager.logout()
                    token = null
                    name = null
                }
            )
        }

        // Profile Image with edit icon
        Box {
            AsyncImage(
                model = profileImage,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            // Edit icon overlay
            IconButton(
                onClick = { /* Edit profile image */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(36.dp)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF6200EE)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username with edit
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = name?:"User",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EE)
            )
            IconButton(onClick = { /* Edit username */ }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Balance Card - Fixed layout
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Total Balance",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = "$${balance}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* Add balance */ },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF6200EE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Balance", fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items - Better approach
        ProfileMenuItem(
            icon = Icons.Default.List,
            title = "Saved Stories",
            onClick = { navController.navigate("library") }
        )

        ProfileMenuItem(
            icon = Icons.Default.Favorite,
            title = "Liked Stories",
            onClick = { /* Navigate to liked stories */ }
        )

        ProfileMenuItem(
            icon = Icons.Default.Person,
            title = "Favorite Writers",
            onClick = { navController.navigate("verify") }
        )

        ProfileMenuItem(
            icon = Icons.Default.Settings,
            title = "Settings",
            onClick = { navController.navigate("auth") }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Logout button at bottom
        TextButton(
            onClick = { showLogoutDialog = true },
            modifier = Modifier.fillMaxWidth()
                .background( Color.White, RoundedCornerShape(12.dp))
        ) {
            Text(
                text = "Logout",
                color = Color.Red,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF6200EE),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}