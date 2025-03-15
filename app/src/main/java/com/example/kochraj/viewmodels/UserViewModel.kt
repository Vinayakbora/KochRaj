package com.example.kochraj.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kochraj.domaim.model.User
import com.example.kochraj.domaim.repository.UserRepository
import com.example.kochraj.utils.Resource
import com.example.kochraj.utils.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern
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

    private val _registrationFormState = MutableStateFlow(RegistrationFormState())
    val registrationFormState: StateFlow<RegistrationFormState> =
        _registrationFormState.asStateFlow()

    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState: StateFlow<LoginFormState> = _loginFormState.asStateFlow()


    private val _isUploadingImage = MutableStateFlow(false)
    val isUploadingImage: StateFlow<Boolean> = _isUploadingImage.asStateFlow()

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

    // Login form validation and submission
    fun updateLoginEmail(email: String) {
        _loginFormState.update {
            it.copy(
                email = email,
                emailError = validateEmail(email).errorMessage
            )
        }
    }

    fun updateLoginPassword(password: String) {
        _loginFormState.update {
            it.copy(
                password = password,
                passwordError = validatePassword(password).errorMessage
            )
        }
    }

    fun login(onLoginSuccess: () -> Unit) {
        val currentState = _loginFormState.value

        // Validate all fields before submission
        val emailResult = validateEmail(currentState.email)
        val passwordResult = validatePassword(currentState.password)

        _loginFormState.update {
            it.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
        }

        // Only proceed if all validations pass
        if (emailResult.successful && passwordResult.successful) {
            viewModelScope.launch {
                _authState.value = AuthState.Loading
                val result = userRepository.signIn(currentState.email, currentState.password)
                _authState.value = when (result) {
                    is Resource.Success -> {
                        onLoginSuccess()
                        AuthState.Success
                    }

                    is Resource.Error -> AuthState.Error(result.message ?: "Unknown error")
                    is Resource.Loading -> AuthState.Loading
                }
            }
        }
    }

    // Registration form validation and submission
    fun updateRegistrationName(name: String) {
        _registrationFormState.update {
            it.copy(
                name = name,
                nameError = validateName(name).errorMessage
            )
        }
    }

    fun updateRegistrationEmail(email: String) {
        _registrationFormState.update {
            it.copy(
                email = email,
                emailError = validateEmail(email).errorMessage
            )
        }
    }

    fun updateRegistrationPhone(phone: String) {
        _registrationFormState.update {
            it.copy(
                phone = phone,
                phoneError = validatePhone(phone).errorMessage
            )
        }
    }

    fun updateRegistrationPassword(password: String) {
        _registrationFormState.update {
            it.copy(
                password = password,
                passwordError = validatePassword(password).errorMessage
            )
        }
    }

    fun register() {
        val currentState = _registrationFormState.value

        // Validate all fields before submission
        val nameResult = validateName(currentState.name)
        val emailResult = validateEmail(currentState.email)
        val phoneResult = validatePhone(currentState.phone)
        val passwordResult = validatePassword(currentState.password)

        _registrationFormState.update {
            it.copy(
                nameError = nameResult.errorMessage,
                emailError = emailResult.errorMessage,
                phoneError = phoneResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
        }

        // Only proceed if all validations pass
        if (nameResult.successful && emailResult.successful &&
            phoneResult.successful && passwordResult.successful
        ) {
            viewModelScope.launch {
                _authState.value = AuthState.Loading
                val result = userRepository.signUp(currentState.email, currentState.password)
                if (result is Resource.Success) {
                    _authState.value = AuthState.Success
                    // Update user profile with name and phone
                    updateName(currentState.name)
                    // Create initial user profile
                    val userId = userRepository.getCurrentUserId()
                    if (userId != null) {
                        val user = User(
                            id = userId,
                            name = currentState.name,
                            email = currentState.email
                        )
                        userRepository.createUser(user)
                    }

                } else {
                    _authState.value = AuthState.Error(
                        (result as Resource.Error).message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            onLogout()
            _authState.value = AuthState.Loading
            val result = userRepository.signOut()
            _authState.value = when (result) {
                is Resource.Success -> AuthState.Idle
                is Resource.Error -> AuthState.Error(result.message ?: "Unknown error")
                is Resource.Loading -> AuthState.Loading
            }
        }
    }

    private fun getUserDetails(userId: String) {
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
        val currentState = _personalDetailsState.value

        // Validate all fields before submission
        val nameResult = validateName(currentState.name)
        val fatherNameResult = validateOptionalName(currentState.fatherName)
        val motherNameResult = validateOptionalName(currentState.motherName)
        val spouseNameResult = validateOptionalName(currentState.spouseName)

        _personalDetailsState.update {
            it.copy(
                nameError = nameResult.errorMessage,
                fatherNameError = fatherNameResult.errorMessage,
                motherNameError = motherNameResult.errorMessage,
                spouseNameError = spouseNameResult.errorMessage
            )
        }

        // Check if there are any validation errors
        if (!nameResult.successful || !fatherNameResult.successful ||
            !motherNameResult.successful || !spouseNameResult.successful
        ) {
            return
        }

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
                languages = personalDetails.languagesKnown,
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
            _isUploadingImage.value = true
            val userId = userRepository.getCurrentUserId() ?: return@launch
            val result = userRepository.uploadUserPhoto(userId, photoBytes)

            _isUploadingImage.value = false

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
                languagesKnown = user.languages,
                skills = user.skills,
                email = user.email
            )
        }
    }

    // Personal Details State Updates with validation
    fun updateName(name: String) {
        val validationResult = validateName(name)
        _personalDetailsState.update {
            it.copy(
                name = name,
                nameError = validationResult.errorMessage
            )
        }
    }

    fun updateFatherName(fatherName: String) {
        val validationResult = validateOptionalName(fatherName)
        _personalDetailsState.update {
            it.copy(
                fatherName = fatherName,
                fatherNameError = validationResult.errorMessage
            )
        }
    }

    fun updateMotherName(motherName: String) {
        val validationResult = validateOptionalName(motherName)
        _personalDetailsState.update {
            it.copy(
                motherName = motherName,
                motherNameError = validationResult.errorMessage
            )
        }
    }

    fun updateSpouseName(spouseName: String) {
        val validationResult = validateOptionalName(spouseName)
        _personalDetailsState.update {
            it.copy(
                spouseName = spouseName,
                spouseNameError = validationResult.errorMessage
            )
        }
    }

    fun updateDateOfBirth(dateOfBirth: String) {
        val validationResult = validateDateOfBirth(dateOfBirth)
        _personalDetailsState.update {
            it.copy(
                dateOfBirth = dateOfBirth,
                dateOfBirthError = validationResult.errorMessage
            )
        }
    }

    fun updatePlaceOfBirth(placeOfBirth: String) {
        _personalDetailsState.update { it.copy(placeOfBirth = placeOfBirth) }
    }

    fun updatePermanentAddress(permanentAddress: String) {
        val validationResult = validateAddress(permanentAddress)
        _personalDetailsState.update {
            it.copy(
                permanentAddress = permanentAddress,
                permanentAddressError = validationResult.errorMessage
            )
        }
    }

    fun updatePresentAddress(presentAddress: String) {
        val validationResult = validateAddress(presentAddress)
        _personalDetailsState.update {
            it.copy(
                presentAddress = presentAddress,
                presentAddressError = validationResult.errorMessage
            )
        }
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

    fun updateLanguagesKnown(languagesKnown: List<String>) {
        _personalDetailsState.update { it.copy(languagesKnown = languagesKnown) }
    }

    fun updateSkills(skills: String) {
        _personalDetailsState.update { it.copy(skills = skills) }
    }

    // Validation functions
    private fun validateName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Name cannot be empty"
            )
        }
        if (name.length < 2) {
            return ValidationResult(
                successful = false,
                errorMessage = "Name must be at least 2 characters long"
            )
        }
        return ValidationResult(successful = true)
    }

    private fun validateOptionalName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(successful = true)
        }
        if (name.length < 2) {
            return ValidationResult(
                successful = false,
                errorMessage = "Name must be at least 2 characters long"
            )
        }
        return ValidationResult(successful = true)
    }

    private fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Email cannot be empty"
            )
        }
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        if (!emailPattern.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Please enter a valid email address"
            )
        }
        return ValidationResult(successful = true)
    }

    private fun validatePhone(phone: String): ValidationResult {
        if (phone.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Phone number cannot be empty"
            )
        }
        if (phone.length != 10) {
            return ValidationResult(
                successful = false,
                errorMessage = "Phone number must be 10 digits"
            )
        }
        if (!phone.all { it.isDigit() }) {
            return ValidationResult(
                successful = false,
                errorMessage = "Phone number must contain only digits"
            )
        }
        return ValidationResult(successful = true)
    }

    private fun validatePassword(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password cannot be empty"
            )
        }
        if (password.length < 6) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password must be at least 6 characters long"
            )
        }
        return ValidationResult(successful = true)
    }

    private fun validateDateOfBirth(dateOfBirth: String): ValidationResult {
        if (dateOfBirth.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Date of birth cannot be empty"
            )
        }
        // You can add more sophisticated date validation here
        return ValidationResult(successful = true)
    }

    private fun validateAddress(address: String): ValidationResult {
        if (address.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Address cannot be empty"
            )
        }
        if (address.length < 5) {
            return ValidationResult(
                successful = false,
                errorMessage = "Address must be at least 5 characters long"
            )
        }
        return ValidationResult(successful = true)
    }

    // State classes
    sealed class AuthState {
        data object Idle : AuthState()
        data object Loading : AuthState()
        data object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    sealed class UserState {
        data object Idle : UserState()
        data object Loading : UserState()
        data class Success(val user: User) : UserState()
        data class Error(val message: String) : UserState()
    }

    data class PersonalDetailsState(
        val name: String = "",
        val nameError: String? = null,
        val fatherName: String = "",
        val fatherNameError: String? = null,
        val motherName: String = "",
        val motherNameError: String? = null,
        val spouseName: String = "",
        val spouseNameError: String? = null,
        val dateOfBirth: String = "",
        val dateOfBirthError: String? = null,
        val placeOfBirth: String = "",
        val permanentAddress: String = "",
        val permanentAddressError: String? = null,
        val presentAddress: String = "",
        val presentAddressError: String? = null,
        val profession: String = "",
        val qualification: String = "",
        val gender: String = "",
        val bloodGroup: String = "",
        val languagesKnown: List<String> = emptyList(),
        val skills: String = "",
        val email: String = ""
    )

    data class RegistrationFormState(
        val name: String = "",
        val nameError: String? = null,
        val email: String = "",
        val emailError: String? = null,
        val phone: String = "",
        val phoneError: String? = null,
        val password: String = "",
        val passwordError: String? = null
    )

    data class LoginFormState(
        val email: String = "",
        val emailError: String? = null,
        val password: String = "",
        val passwordError: String? = null
    )
}

