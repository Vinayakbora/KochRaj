package com.example.kochraj.domaim.model

data class Favorite(
    val id: String = "", // Auto-generated ID
    val userId: String = "", // ID of the user who added the favorite
    val favoriteUserId: String = "", // ID of the user who was favorited
    val timestamp: Long = System.currentTimeMillis()
)

