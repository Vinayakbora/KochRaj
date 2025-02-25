package com.example.kochraj.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kochraj.screens.LoginScreen
import com.example.kochraj.screens.RegistrationScreen
import com.example.kochraj.screens.MainScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LoginScreen.route) {
        composable(Routes.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Routes.RegistrationScreen.route) {
            RegistrationScreen(navController)
        }
        composable(Routes.HomeScreen.route) {
            MainScreen(navController)
        }
    }
}
