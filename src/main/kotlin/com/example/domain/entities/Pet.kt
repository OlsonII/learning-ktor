package com.example.domain.entities

import com.example.domain.base.Entity
import com.example.domain.enums.PetSize

data class Pet(val name: String, val ownerId: String, var weightInKg: Float, var kind: String, var petSize: PetSize)
    : Entity("$ownerId-$name") {
}