package com.example.kochraj.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.ui.theme.EMPTY_STRING
import com.example.kochraj.viewmodels.UserSearchViewModel
import com.example.kochraj.widgets.RecommendedSearchCard

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: UserSearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Aztec)
            .padding(top = 16.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = { Text("Search for users...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.large
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search results
        when (searchState) {
            is UserSearchViewModel.SearchState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UserSearchViewModel.SearchState.Success -> {
                val users = (searchState as UserSearchViewModel.SearchState.Success).users

                if (users.isEmpty()) {
                    // No results found
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.isEmpty())
                                "No users available"
                            else
                                "No users found matching '$searchQuery'",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    // Show user list
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = if (searchQuery.isEmpty()) "All Users" else "Search Results",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            items(users) { user ->
                                RecommendedSearchCard(
                                    name = user.name ?: EMPTY_STRING,
                                    imageUrl = "/placeholder.svg?height=80&width=80",
                                    onAppointmentClick = { },
                                    onCallClick = {uriHandler.openUri("tel:+91${user.phone}") },
                                    onMessageClick = { uriHandler.openUri("mailto:${user.email}") },
                                    onCardClick = {
                                        viewModel.selectUser(user)
                                        navController.navigate("UserDetailsScreen/${user.id}")
                                    },
                                    location = user.presentAddress ?: EMPTY_STRING,
                                    profession = user.profession ?: EMPTY_STRING
                                )
                            }
                        }
                    }
                }
            }

            is UserSearchViewModel.SearchState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = (searchState as UserSearchViewModel.SearchState.Error).message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            viewModel.updateSearchQuery("") // This will trigger fetchAllUsers()
                        }) {
                            Text("Retry")
                        }
                    }
                }
            }

        }
    }
}

