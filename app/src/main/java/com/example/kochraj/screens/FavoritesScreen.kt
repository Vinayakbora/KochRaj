package com.example.kochraj.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kochraj.navigation.Routes
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.widgets.RecommendedSearchCard

@Composable
fun FavoritesScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.background(color = Aztec)
    ) {
        Text(
            text = "Saved Profiles",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(5) { index ->
                RecommendedSearchCard(
                    name = if (index == 0) "Dr. Deepak Ray" else "Aniket Sarkar",
                    location = "Guwahati, Assam",
                    occupation = "Doctor",
                    rating = 4.8f,
                    reviews = 102,
                    imageUrl = "/placeholder.svg?height=80&width=80",
                    onAppointmentClick = { },
                    onCallClick = { },
                    onMessageClick = { },
                    onCardClick = { navController.navigate(Routes.ProductDetailsScreen.route) }
                )
            }
        }
    }
}

