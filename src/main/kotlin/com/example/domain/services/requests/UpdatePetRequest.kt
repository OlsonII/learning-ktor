package com.example.domain.services.requests

import com.example.domain.enums.PetSize

class UpdatePetRequest (
    val weight: Float,
    val kind: String,
    val petSize: PetSize) {}