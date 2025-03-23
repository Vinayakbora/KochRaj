package com.example.kochraj.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.ui.theme.EMPTY_STRING
import com.example.kochraj.viewmodels.FavoritesViewModel
import com.example.kochraj.viewmodels.UserSearchViewModel
import com.example.kochraj.widgets.RecommendedSearchCard

@Composable
fun FavoritesScreen(
    navController: NavController,
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    searchViewModel: UserSearchViewModel = hiltViewModel()
) {
    val favoritesState by favoritesViewModel.favoritesState.collectAsState()
    val favoriteStatusMap by favoritesViewModel.favoriteStatusMap.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        favoritesViewModel.loadFavorites()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Aztec)
            .padding(top = 16.dp)
    ) {
        // Header
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Favorites content
        when (favoritesState) {
            is FavoritesViewModel.FavoritesState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is FavoritesViewModel.FavoritesState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No favorites yet",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Add users to your favorites to see them here",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }
            }
            is FavoritesViewModel.FavoritesState.Success -> {
                val favorites = (favoritesState as FavoritesViewModel.FavoritesState.Success).favorites

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(favorites) { user ->
                        val isFavorite = favoriteStatusMap[user.id] ?: false

                        // If we don't have the status yet, check it
                        LaunchedEffect(user.id) {
                            if (!favoriteStatusMap.containsKey(user.id)) {
                                favoritesViewModel.checkFavoriteStatus(user.id)
                            }
                        }
                        RecommendedSearchCard(
                            name = user.name ?: EMPTY_STRING,
                            imageUrl = "/placeholder.svg?height=80&width=80",
                            onCallClick = {uriHandler.openUri("tel:+91${user.phone}") },
                            onMessageClick = { uriHandler.openUri("mailto:${user.email}") },
                            onCardClick = {
                                searchViewModel.selectUser(user)
                                navController.navigate("UserDetailsScreen/${user.id}")
                            },
                            isFavourite = isFavorite,
                            onFavoriteToggle = {
                                favoritesViewModel.toggleFavorite(user)
                            },
                            location = user.presentAddress ?: EMPTY_STRING,
                            profession = user.profession ?: EMPTY_STRING
                        )
                    }
                }
            }
            is FavoritesViewModel.FavoritesState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No favorites yet",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Add users to your favorites to see them here",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}


