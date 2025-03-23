package com.example.kochraj.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kochraj.domaim.model.User
import com.example.kochraj.domaim.repository.UserRepository
import com.example.kochraj.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val TAG = "UserSearchViewModel"

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Loading)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser.asStateFlow()

    init {
        // Set up search with debounce
        viewModelScope.launch {
            searchQuery
                .debounce(300) // Wait for 300ms of inactivity before searching
                .distinctUntilChanged() // Only proceed if the query changed
                .collect { query ->
                    if (query.isBlank()) {
                        fetchAllUsers() // Fetch all users when query is blank
                    } else {
                        searchUsers(query) // Search users with query
                    }
                }
        }

        // Initially fetch all users
        fetchAllUsers()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.update { query }
    }

    fun selectUser(user: User) {
        _selectedUser.update { user }
    }

    fun clearSelectedUser() {
        _selectedUser.update { null }
    }

    private fun fetchAllUsers() {
        viewModelScope.launch {
            Log.d(TAG, "Fetching all users")
            _searchState.value = SearchState.Loading

            try {
                userRepository.getAllUsers().collect { result ->
                    _searchState.value = when (result) {
                        is Resource.Success -> {
                            val users = result.data ?: emptyList()
                            Log.d(TAG, "Fetched ${users.size} users")
                            SearchState.Success(users)
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "Error fetching users: ${result.message}")
                            SearchState.Error(result.message ?: "Unknown error occurred")
                        }
                        is Resource.Loading -> SearchState.Loading
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception fetching users: ${e.message}", e)
                _searchState.value = SearchState.Error("Failed to fetch users: ${e.message}")
            }
        }
    }

    private fun searchUsers(query: String) {
        viewModelScope.launch {
            Log.d(TAG, "Searching users with query: $query")
            _searchState.value = SearchState.Loading

            try {
                userRepository.searchUsers(query).collect { result ->
                    _searchState.value = when (result) {
                        is Resource.Success -> {
                            val users = result.data ?: emptyList()
                            Log.d(TAG, "Search returned ${users.size} results")
                            SearchState.Success(users)
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "Search error: ${result.message}")
                            SearchState.Error(result.message ?: "Unknown error occurred")
                        }
                        is Resource.Loading -> SearchState.Loading
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during search: ${e.message}", e)
                _searchState.value = SearchState.Error("Search failed: ${e.message}")
            }
        }
    }

    sealed class SearchState {
        data object Loading : SearchState()
        data class Success(val users: List<User>) : SearchState()
        data class Error(val message: String) : SearchState()
    }
}

