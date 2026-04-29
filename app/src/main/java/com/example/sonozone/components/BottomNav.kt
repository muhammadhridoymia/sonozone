package com.example.sonozone.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sonozone.R


// ── Color tokens (keep in sync with HomeScreen) ───────────────
private val BgNav       = Color(0xFF13121A)
private val BorderNav   = Color(0xFF26242E)
private val Accent      = Color(0xFFA78BFA)
private val TextActive  = Color(0xFFF0EEF8)
private val TextInactive = Color(0xFF5E5C70)

// ── Nav item model ────────────────────────────────────────────
private data class NavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun BottomNavigation(navController: NavController) {

    val items = listOf(
        NavItem("home",    "Home",    Icons.Filled.Home,   Icons.Outlined.Home),
        NavItem("lines",   "Lines",    Icons.Filled.List,     Icons.Outlined.List),
        NavItem("player/1",  "Player",  Icons.Filled.PlayArrow, Icons.Outlined.PlayArrow),
        NavItem("search",  "Search",  Icons.Filled.Search, Icons.Outlined.Search),
        NavItem("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person),
    )

    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    NavigationBar(
        containerColor = BgNav,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(item.label, fontSize = 11.sp)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = Accent,
                    selectedTextColor   = Accent,
                    unselectedIconColor = TextInactive,
                    unselectedTextColor = TextInactive,
                    indicatorColor      = Color(0xFF1F1D2B)
                )
            )
        }
    }
}