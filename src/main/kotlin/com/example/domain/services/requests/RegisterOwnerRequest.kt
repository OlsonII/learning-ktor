package com.example.domain.services.requests

data class RegisterOwnerRequest (
    val id: String,
    val firstName: String,
    val secondName: String,
    val address: String,
    val phone: String
) {}