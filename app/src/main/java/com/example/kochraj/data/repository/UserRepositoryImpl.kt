package com.example.kochraj.data.repository

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
            auth.createUserWithEmailAndPassword(email, password).await()
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
            usersCollection.document(userId).set(user).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun getUserById(userId: String): Flow<Resource<User>> = callbackFlow {
        val snapshotListener = usersCollection.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Resource.Error(error.message ?: "An unknown error occurred"))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                if (user != null) {
                    trySend(Resource.Success(user))
                } else {
                    trySend(Resource.Error("Failed to parse user data"))
                }
            } else {
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
            val storageRef = storage.reference.child("users/$userId/profile.jpg")
            val uploadTask = storageRef.putBytes(photoBytes).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            Resource.Success(downloadUrl)
        } catch (e: Exception) {
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

