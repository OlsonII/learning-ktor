package com.example.domain.services

import com.example.domain.entities.Pet
import com.example.domain.services.requests.RegisterPetRequest
import com.example.domain.services.requests.UpdatePetRequest

interface IPetServices {
    fun registerPet(request: RegisterPetRequest) : Pet
    fun updatePet(petName: String, ownerId: String, request: UpdatePetRequest) : Pet
    fun findSpecifyPet(name: String, ownerId: String) : Pet
    fun findAllPets() : List<Pet>
}