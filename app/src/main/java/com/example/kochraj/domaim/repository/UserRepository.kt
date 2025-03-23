package com.example.kochraj.domaim.repository

import com.example.kochraj.domaim.model.User
import com.example.kochraj.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    // Authentication
    suspend fun signIn(email: String, password: String): Resource<Boolean>
    suspend fun signUp(email: String, password: String): Resource<Boolean>
    suspend fun signOut(): Resource<Boolean>
    fun isUserAuthenticated(): Boolean
    fun getCurrentUserId(): String?

    // User data
    suspend fun createUser(user: User): Resource<Boolean>
    suspend fun updateUser(user: User): Resource<Boolean>
    suspend fun getUserById(userId: String): Flow<Resource<User>>
    suspend fun getAllUsers(): Flow<Resource<List<User>>>
    suspend fun deleteUser(userId: String): Resource<Boolean>
    suspend fun updateUserPhotoUrl(userId: String, photoUrl: String): Resource<Boolean>
    suspend fun searchUsers(query: String): Flow<Resource<List<User>>>

    // File upload
    suspend fun uploadUserPhoto(userId: String, photoBytes: ByteArray): Resource<String>
    suspend fun uploadDocument(userId: String, documentName: String, documentBytes: ByteArray): Resource<String>
}