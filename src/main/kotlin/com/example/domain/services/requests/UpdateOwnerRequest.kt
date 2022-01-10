package com.example.domain.services.requests

data class UpdateOwnerRequest (
    val firstName: String,
    val secondName: String,
    val address: String,
    val phone: String
) {}