package com.example.kochraj.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kochraj.navigation.Routes
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.widgets.RecommendedSearchCard

@Composable
fun FavoritesScreen(navController: NavHostController) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Aztec)
    ) {
        Text(
            text = "Saved Profiles",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
//        LazyColumn {
//            items(5) { index ->
//
//                RecommendedSearchCard(
//                    name = if (index == 0) "Dr. Deepak Ray" else "Aniket Sarkar",
//                    location = "Guwahati, Assam",
//                    occupation = "Doctor",
//                    rating = 4.8f,
//                    reviews = 102,
//                    imageUrl = "/placeholder.svg?height=80&width=80",
//                    onAppointmentClick = { },
//                    onCallClick = { uriHandler.openUri("tel:+918724974038") },
//                    onMessageClick = { uriHandler.openUri("mailto:support@kochraj.com") },
//                    onCardClick = { navController.navigate(Routes.UserDetailsScreen.route) },
//                    isFavourite = true
//                )
//            }
//        }
    }
}

