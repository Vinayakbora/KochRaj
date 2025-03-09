package com.example.kochraj.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kochraj.navigation.Routes
import com.example.kochraj.widgets.BottomNavBar

@Composable
fun MainScreen(mainNavController: NavController) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.HomeScreen.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Routes.HomeScreen.route) { HomeScreen(navController) }
            composable(Routes.SearchScreen.route) { SearchScreen(navController) }
            composable(Routes.PersonalDetailsScreen.route) { PersonalDetailsScreen(navController) }
            composable(Routes.FavoritesScreen.route) { FavoritesScreen(navController) }
            composable(Routes.ProductDetailsScreen.route) { ProductDetailsScreen(navController) }
            composable(Routes.ProfileScreen.route) { ProfileScreen(mainNavController, navController) }
        }
    }
}

