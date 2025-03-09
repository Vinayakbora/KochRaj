package com.example.kochraj.viewmodels

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
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _userState = MutableStateFlow<UserState>(UserState.Idle)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _personalDetailsState = MutableStateFlow(PersonalDetailsState())
    val personalDetailsState: StateFlow<PersonalDetailsState> = _personalDetailsState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        val isAuthenticated = userRepository.isUserAuthenticated()
        if (isAuthenticated) {
            val userId = userRepository.getCurrentUserId()
            if (userId != null) {
                getUserDetails(userId)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.signIn(email, password)
            _authState.value = when (result) {
                is Resource.Success -> AuthState.Success
                is Resource.Loading -> AuthState.Loading
                is Resource.Error -> AuthState.Error(result.message ?: "Unknown error")

            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.signUp(email, password)
            _authState.value = when (result) {
                is Resource.Success -> AuthState.Success
                is Resource.Error -> AuthState.Error(result.message ?: "Unknown error")
                is Resource.Loading -> AuthState.Loading
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.signOut()
            _authState.value = when (result) {
                is Resource.Success -> AuthState.Idle
                is Resource.Error -> AuthState.Error(result.message ?: "Unknown error")
                is Resource.Loading -> AuthState.Loading
            }
        }
    }

    fun getUserDetails(userId: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            userRepository.getUserById(userId).collect { result ->
                _userState.value = when (result) {
                    is Resource.Success -> {
                        result.data?.let { user ->
                            updatePersonalDetailsFromUser(user)
                            UserState.Success(user)
                        } ?: UserState.Error("User data is null")
                    }
                    is Resource.Error -> UserState.Error(result.message ?: "Unknown error")
                    is Resource.Loading -> UserState.Loading
                }
            }
        }
    }

    fun savePersonalDetails() {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            val personalDetails = _personalDetailsState.value
            val userId = userRepository.getCurrentUserId() ?: return@launch

            val user = User(
                id = userId,
                name = personalDetails.name,
                fatherName = personalDetails.fatherName,
                motherName = personalDetails.motherName,
                spouseName = personalDetails.spouseName,
                dateOfBirth = personalDetails.dateOfBirth,
                placeOfBirth = personalDetails.placeOfBirth,
                permanentAddress = personalDetails.permanentAddress,
                presentAddress = personalDetails.presentAddress,
                profession = personalDetails.profession,
                qualification = personalDetails.qualification,
                gender = personalDetails.gender,
                bloodGroup = personalDetails.bloodGroup,
                languages = personalDetails.languages,
                skills = personalDetails.skills,
                email = personalDetails.email
            )

            val result = userRepository.updateUser(user)
            _userState.value = when (result) {
                is Resource.Success -> UserState.Success(user)
                is Resource.Error -> UserState.Error(result.message ?: "Unknown error")
                is Resource.Loading -> UserState.Loading
            }
        }
    }

    fun uploadProfilePhoto(photoBytes: ByteArray) {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId() ?: return@launch
            val result = userRepository.uploadUserPhoto(userId, photoBytes)

            if (result is Resource.Success && result.data != null) {
                val photoUrl = result.data
                // Update user with new photo URL
                _userState.value.let { state ->
                    if (state is UserState.Success) {
                        val updatedUser = state.user.copy(photoUrl = photoUrl)
                        userRepository.updateUser(updatedUser)
                    }
                }
            }
        }
    }

    private fun updatePersonalDetailsFromUser(user: User) {
        _personalDetailsState.update {
            it.copy(
                name = user.name,
                fatherName = user.fatherName,
                motherName = user.motherName,
                spouseName = user.spouseName,
                dateOfBirth = user.dateOfBirth,
                placeOfBirth = user.placeOfBirth,
                permanentAddress = user.permanentAddress,
                presentAddress = user.presentAddress,
                profession = user.profession,
                qualification = user.qualification,
                gender = user.gender,
                bloodGroup = user.bloodGroup,
                languages = user.languages,
                skills = user.skills,
                email = user.email
            )
        }
    }

    // Personal Details State Updates
    fun updateName(name: String) {
        _personalDetailsState.update { it.copy(name = name) }
    }

    fun updateFatherName(fatherName: String) {
        _personalDetailsState.update { it.copy(fatherName = fatherName) }
    }

    fun updateMotherName(motherName: String) {
        _personalDetailsState.update { it.copy(motherName = motherName) }
    }

    fun updateSpouseName(spouseName: String) {
        _personalDetailsState.update { it.copy(spouseName = spouseName) }
    }

    fun updateDateOfBirth(dateOfBirth: String) {
        _personalDetailsState.update { it.copy(dateOfBirth = dateOfBirth) }
    }

    fun updatePlaceOfBirth(placeOfBirth: String) {
        _personalDetailsState.update { it.copy(placeOfBirth = placeOfBirth) }
    }

    fun updatePermanentAddress(permanentAddress: String) {
        _personalDetailsState.update { it.copy(permanentAddress = permanentAddress) }
    }

    fun updatePresentAddress(presentAddress: String) {
        _personalDetailsState.update { it.copy(presentAddress = presentAddress) }
    }

    fun updateProfession(profession: String) {
        _personalDetailsState.update { it.copy(profession = profession) }
    }

    fun updateQualification(qualification: String) {
        _personalDetailsState.update { it.copy(qualification = qualification) }
    }

    fun updateGender(gender: String) {
        _personalDetailsState.update { it.copy(gender = gender) }
    }

    fun updateBloodGroup(bloodGroup: String) {
        _personalDetailsState.update { it.copy(bloodGroup = bloodGroup) }
    }

    fun updateLanguagesKnown(languages: List<String>) {
        _personalDetailsState.update { it.copy(languages = languages) }
    }

    fun updateSkills(skills: String) {
        _personalDetailsState.update { it.copy(skills = skills) }
    }

    // State classes
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    sealed class UserState {
        object Idle : UserState()
        object Loading : UserState()
        data class Success(val user: User) : UserState()
        data class Error(val message: String) : UserState()
    }

    data class PersonalDetailsState(
        val name: String = "",
        val fatherName: String = "",
        val motherName: String = "",
        val spouseName: String = "",
        val dateOfBirth: String = "",
        val placeOfBirth: String = "",
        val permanentAddress: String = "",
        val presentAddress: String = "",
        val profession: String = "",
        val qualification: String = "",
        val gender: String = "",
        val bloodGroup: String = "",
        val languages: List<String> = emptyList(),
        val skills: String = "",
        val email: String = ""
    )
}

