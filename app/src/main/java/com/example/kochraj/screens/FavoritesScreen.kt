package com.example.kochraj.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.kochraj.navigation.Routes
import com.example.kochraj.widgets.RecommendedSearchCard

@Composable
fun FavoritesScreen(navController: NavHostController) {
    LazyColumn {
        items(5) { index ->
            RecommendedSearchCard(
                name = if (index == 0) "Dr. Joynal Abedin Faruk" else "Dr. Tahmid Islam Hafez",
                specialty = "Bellevue Hospital Center",
                rating = 4.8f,
                reviews = 102,
                price = 30.00,
                imageUrl = "/placeholder.svg?height=80&width=80",
                onAppointmentClick = { },
                onCallClick = { },
                onMessageClick = { },
                onCardClick = { navController.navigate(Routes.ProductDetailsScreen.route) }
            )
        }
    }
}

