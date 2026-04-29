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
import com.example.sonozone.ui.screens.LibraryScreen
import com.example.sonozone.ui.screens.ProfileScreen
import com.example.sonozone.ui.screens.SearchScreen
import com.example.sonozone.ui.screens.LinesScreen
import com.example.sonozone.ui.screens.PlayerScreen
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
                            HomeScreen( navController = navController, modifier = Modifier.fillMaxSize())
                        }

                        composable("library") {
                            LibraryScreen() // temp
                        }

                        composable("profile") {
                            ProfileScreen() // temp
                        }
                        composable("search") {
                            SearchScreen() // temp
                        }
                        composable("lines") {
                            LinesScreen() // temp
                        }
                        composable("player/{storyId}") { backStackEntry ->
                            val storyId = backStackEntry.arguments?.getString("storyId") ?: "1"
                            PlayerScreen(storyId, onStoryClick = { storyId -> navController.navigate("player/$storyId") })
                        }
                    }
                }
            }
        }
    }
}