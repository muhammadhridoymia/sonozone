package com.example.sonozone

import android.content.Intent
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
import com.example.sonozone.Auth.AuthScreen
import com.example.sonozone.Auth.CodeVerifyScreen
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
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                        if (currentRoute?.startsWith("player") != true) {
                            BottomNavigation(navController)
                        }
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
                        composable("verify"){
                            CodeVerifyScreen(navController = navController)
                        }
                        composable("auth"){
                            AuthScreen(navController = navController)
                        }

                        composable("library") {
                            LibraryScreen() // temp
                        }

                        composable("profile") {
                            ProfileScreen( navController = navController, modifier = Modifier.fillMaxSize()) // temp
                        }
                        composable("search") {
                            SearchScreen( navController = navController) // temp
                        }
                        composable("lines") {
                            LinesScreen() // temp
                        }
                        composable("player/{storyId}") { backStackEntry ->
                            val storyId = backStackEntry.arguments?.getString("storyId") ?: "1"
                            PlayerScreen(storyId, navController=navController)
                        }
                    }
                }
            }
        }
    }
}