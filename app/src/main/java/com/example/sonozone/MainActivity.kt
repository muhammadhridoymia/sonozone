package com.example.sonozone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.sonozone.ui.components.BottomNavigation
import com.example.sonozone.ui.screens.HomeScreen
import com.example.sonozone.ui.theme.SonoZoneTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SonoZoneTheme {

                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigation(navController)
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable("home") {
                            HomeScreen()
                        }

                        composable("library") {
                            HomeScreen() // temp
                        }

                        composable("profile") {
                            HomeScreen() // temp
                        }
                    }
                }
            }
        }
    }
}