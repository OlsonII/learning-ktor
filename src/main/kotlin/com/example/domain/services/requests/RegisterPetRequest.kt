package com.example.domain.services.requests

import com.example.domain.enums.PetSize

data class RegisterPetRequest(
    val name: String,
    val ownerId: String,
    val weight: Float,
    val kind: String,
    val petSize: PetSize) {}