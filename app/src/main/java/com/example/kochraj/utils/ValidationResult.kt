package com.example.kochraj.utils

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)