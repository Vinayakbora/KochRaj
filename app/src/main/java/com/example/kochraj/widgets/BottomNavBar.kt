package com.example.kochraj.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.ui.theme.MintTulip

@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar(
        containerColor = Aztec
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemColors(
                selectedIconColor = MintTulip,
                selectedTextColor = MintTulip,
                selectedIndicatorColor = MintTulip,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                disabledTextColor = Color.Gray,
                disabledIconColor =  Color.Gray,
            ),
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            colors = NavigationBarItemColors(
                selectedIconColor = MintTulip,
                selectedTextColor = MintTulip,
                selectedIndicatorColor = MintTulip,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                disabledTextColor = Color.Gray,
                disabledIconColor =  Color.Gray,
            ),
            selected = currentRoute == "search",
            onClick = { navController.navigate("search") }
        )

//        NavigationBarItem(
//            icon = { Icon(Icons.Default.AddCircle, contentDescription = "AddPersonalInfo") },
//            label = { Text("Add") },
//            selected = currentRoute == "PersonalDetailsScreen",
//            onClick = { navController.navigate("PersonalDetailsScreen") }
//        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            colors = NavigationBarItemColors(
                selectedIconColor = MintTulip,
                selectedTextColor = MintTulip,
                selectedIndicatorColor = MintTulip,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                disabledTextColor = Color.Gray,
                disabledIconColor =  Color.Gray,
            ),
            selected = currentRoute == "favorites",
            onClick = { navController.navigate("favorites") }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = NavigationBarItemColors(
                selectedIconColor = MintTulip,
                selectedTextColor = MintTulip,
                selectedIndicatorColor = MintTulip,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                disabledTextColor = Color.Gray,
                disabledIconColor =  Color.Gray,
            ),
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") }
        )
    }
}

