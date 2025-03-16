package com.example.kochraj.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kochraj.navigation.Routes
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.ui.theme.EMPTY_STRING
import com.example.kochraj.widgets.CustomSearchBar
import com.example.kochraj.widgets.RecommendedSearchCard

@Composable
fun SearchScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf(EMPTY_STRING) }
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .background(color = Aztec)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CustomSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            item {
                Text(
                    text = "Most Common Searches",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            items(5) { index ->
                RecommendedSearchCard(
                    name = if (index == 0) "Dr. Deepak Ray" else "Aniket Sarkar",
                    location = "Guwahati, Assam",
                    occupation = "Doctor",
                    rating = 4.8f,
                    reviews = 102,
                    imageUrl = "/placeholder.svg?height=80&width=80",
                    onAppointmentClick = { },
                    onCallClick = { uriHandler.openUri("tel:+918724974038") },
                    onMessageClick = { uriHandler.openUri("mailto:support@kochraj.com") },
                    onCardClick = { navController.navigate(Routes.ProductDetailsScreen.route) }
                )
            }
        }
    }
}