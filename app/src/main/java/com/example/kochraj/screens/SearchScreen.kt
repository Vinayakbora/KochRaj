package com.example.kochraj.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kochraj.navigation.Routes
import com.example.kochraj.ui.theme.EMPTY_STRING
import com.example.kochraj.widgets.CustomSearchBar
import com.example.kochraj.widgets.RecommendedSearchCard

@Composable
fun SearchScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf(EMPTY_STRING) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CustomSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recommended Doctors",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

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
}