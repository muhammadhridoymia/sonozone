package com.example.sonozone.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavigation(navController: NavController) {

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("home")
            },
            icon = { Text("🏠") },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("library")
            },
            icon = { Text("📚") },
            label = { Text("Library") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("profile")
            },
            icon = { Text("👤") },
            label = { Text("Profile") }
        )
    }
}