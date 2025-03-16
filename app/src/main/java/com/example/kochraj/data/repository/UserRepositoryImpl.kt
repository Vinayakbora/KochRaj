package com.example.kochraj.data.repository

import android.util.Log
import com.example.kochraj.domaim.model.User
import com.example.kochraj.domaim.repository.UserRepository
import com.example.kochraj.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserRepository {

    private val TAG = "UserRepositoryImpl"
    private val usersCollection = firestore.collection("users")

    override suspend fun signIn(email: String, password: String): Resource<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun signUp(email: String, password: String): Resource<Boolean> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun signOut(): Resource<Boolean> {
        return try {
            auth.signOut()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override suspend fun createUser(user: User): Resource<Boolean> {
        return try {
            val userId = getCurrentUserId() ?: return Resource.Error("User not authenticated")
            val userWithId = user.copy(id = userId)
            usersCollection.document(userId).set(userWithId).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun updateUser(user: User): Resource<Boolean> {
        return try {
            val userId = user.id.ifEmpty { getCurrentUserId() ?: return Resource.Error("User not authenticated") }
            Log.d(TAG, "Updating user with ID: $userId and data: $user")
            usersCollection.document(userId).set(user).await()
            Log.d(TAG, "User updated successfully")
            Resource.Success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user: ${e.message}", e)
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun updateUserPhotoUrl(userId: String, photoUrl: String): Resource<Boolean> {
        return try {
            Log.d(TAG, "Updating user photo URL: $photoUrl for user: $userId")
            // Get current user data first
            val userDoc = usersCollection.document(userId).get().await()
            val user = userDoc.toObject(User::class.java)

            if (user != null) {
                // Update only the photo URL field
                val updatedUser = user.copy(photoUrl = photoUrl)
                usersCollection.document(userId).set(updatedUser).await()
                Log.d(TAG, "User photo URL updated successfully")
                Resource.Success(true)
            } else {
                Log.e(TAG, "User not found when updating photo URL")
                Resource.Error("User not found")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user photo URL: ${e.message}", e)
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun getUserById(userId: String): Flow<Resource<User>> = callbackFlow {
        val snapshotListener = usersCollection.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error getting user: ${error.message}", error)
                trySend(Resource.Error(error.message ?: "An unknown error occurred"))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                if (user != null) {
                    Log.d(TAG, "User data retrieved: $user")
                    trySend(Resource.Success(user))
                } else {
                    Log.e(TAG, "Failed to parse user data")
                    trySend(Resource.Error("Failed to parse user data"))
                }
            } else {
                Log.e(TAG, "User not found")
                trySend(Resource.Error("User not found"))
            }
        }

        awaitClose { snapshotListener.remove() }
    }

    override suspend fun getAllUsers(): Flow<Resource<List<User>>> = callbackFlow {
        val snapshotListener = usersCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Resource.Error(error.message ?: "An unknown error occurred"))
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val users = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
                trySend(Resource.Success(users))
            } else {
                trySend(Resource.Error("No users found"))
            }
        }

        awaitClose { snapshotListener.remove() }
    }

    override suspend fun deleteUser(userId: String): Resource<Boolean> {
        return try {
            usersCollection.document(userId).delete().await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun uploadUserPhoto(userId: String, photoBytes: ByteArray): Resource<String> {
        return try {
            Log.d(TAG, "Starting photo upload for user: $userId")
            val storageRef = storage.reference.child("users/$userId/profile.jpg")

            // Upload the image
            val uploadTask = storageRef.putBytes(photoBytes).await()
            Log.d(TAG, "Photo uploaded successfully, getting download URL")

            // Get the download URL
            val downloadUrl = storageRef.downloadUrl.await().toString()
            Log.d(TAG, "Got download URL: $downloadUrl")

            // Update the user's photoUrl field in Firestore
            val updateResult = updateUserPhotoUrl(userId, downloadUrl)
            if (updateResult is Resource.Error) {
                Log.e(TAG, "Failed to update user photo URL in Firestore: ${updateResult.message}")
                return Resource.Error("Failed to update user photo URL: ${updateResult.message}")
            }

            Resource.Success(downloadUrl)
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading photo: ${e.message}", e)
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun uploadDocument(userId: String, documentName: String, documentBytes: ByteArray): Resource<String> {
        return try {
            val storageRef = storage.reference.child("users/$userId/documents/$documentName")
            val uploadTask = storageRef.putBytes(documentBytes).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            Resource.Success(downloadUrl)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}

