package com.example.kochraj.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kochraj.domaim.model.User
import com.example.kochraj.domaim.repository.UserRepository
import com.example.kochraj.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val TAG = "FavoritesViewModel"

    private val _favoritesState = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val favoritesState: StateFlow<FavoritesState> = _favoritesState.asStateFlow()

    private val _favoriteStatusMap = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val favoriteStatusMap: StateFlow<Map<String, Boolean>> = _favoriteStatusMap.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            Log.d(TAG, "Loading favorites")
            _favoritesState.value = FavoritesState.Loading

            try {
                userRepository.getFavoriteUsers().collect { result ->
                    _favoritesState.value = when (result) {
                        is Resource.Success -> {
                            val users = result.data ?: emptyList()
                            Log.d(TAG, "Loaded ${users.size} favorite users")

                            // Update favorite status map
                            val statusMap = users.associate { it.id to true }
                            _favoriteStatusMap.update { statusMap }

                            if (users.isEmpty()) {
                                FavoritesState.Empty
                            } else {
                                FavoritesState.Success(users)
                            }
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "Error loading favorites: ${result.message}")
                            FavoritesState.Error(result.message ?: "Unknown error occurred")
                        }
                        is Resource.Loading -> FavoritesState.Loading
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception loading favorites: ${e.message}", e)
                _favoritesState.value = FavoritesState.Error("Failed to load favorites: ${e.message}")
            }
        }
    }

    fun toggleFavorite(user: User) {
        viewModelScope.launch {
            val userId = user.id
            val isFavorite = _favoriteStatusMap.value[userId] ?: false

            try {
                val result = if (isFavorite) {
                    Log.d(TAG, "Removing favorite: $userId")
                    userRepository.removeFavorite(userId)
                } else {
                    Log.d(TAG, "Adding favorite: $userId")
                    userRepository.addFavorite(userId)
                }

                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "Toggle favorite successful for user: $userId")
                        // Update the status map immediately for UI responsiveness
                        _favoriteStatusMap.update { currentMap ->
                            currentMap.toMutableMap().apply {
                                this[userId] = !isFavorite
                            }
                        }
                        // Reload favorites to refresh the list
                        loadFavorites()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Error toggling favorite: ${result.message}")
                    }
                    is Resource.Loading -> {
                        // Do nothing
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception toggling favorite: ${e.message}", e)
            }
        }
    }

    fun checkFavoriteStatus(userId: String) {
        viewModelScope.launch {
            try {
                userRepository.isFavorite(userId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            val isFavorite = result.data ?: false
                            _favoriteStatusMap.update { currentMap ->
                                currentMap.toMutableMap().apply {
                                    this[userId] = isFavorite
                                }
                            }
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "Error checking favorite status: ${result.message}")
                        }
                        is Resource.Loading -> {
                            // Do nothing
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception checking favorite status: ${e.message}", e)
            }
        }
    }

    sealed class FavoritesState {
        data object Loading : FavoritesState()
        data object Empty : FavoritesState()
        data class Success(val favorites: List<User>) : FavoritesState()
        data class Error(val message: String) : FavoritesState()
    }
}

