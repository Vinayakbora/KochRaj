package com.example.kochraj.domaim.model

data class User(
    val id: String = "",
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
    val phone: String = "",
    val email: String = "",
    val photoUrl: String = ""
)