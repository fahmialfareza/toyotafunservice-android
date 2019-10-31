package com.dinokeylas.toyotafunservice.model

data class User(
    val userName: String = "",
    val fullName: String = "",
    val email: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String = "",
    val password: String = ""
)