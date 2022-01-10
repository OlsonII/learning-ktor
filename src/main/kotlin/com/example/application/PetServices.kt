package com.example.application

import com.example.domain.entities.Pet
import com.example.domain.services.IPetServices
import com.example.domain.services.requests.RegisterPetRequest
import com.example.domain.services.requests.UpdatePetRequest
import com.example.infrastructure.base.UnitOfWork

class PetServices(private val unitOfWork: UnitOfWork) : IPetServices {
    override fun registerPet(request: RegisterPetRequest) : Pet {
        val owner = unitOfWork.ownerRepository.find(request.ownerId) ?: throw Exception("Owner not found")
        val pet = Pet(request.name, owner.id, request.weight, request.kind, request.petSize)
        unitOfWork.petRepository.register(pet)
        return pet
    }

    override fun updatePet(petName: String, ownerId: String, request: UpdatePetRequest) : Pet {
        val petId = "$ownerId-$petName"
        val pet = unitOfWork.petRepository.find(petId) ?: throw Exception("Pet not found")
        pet.weightInKg = if (request.weight != 0f) request.weight else pet.weightInKg
        pet.kind = if (request.kind != "") request.kind else pet.kind
        pet.petSize = request.petSize
        return pet
    }

    override fun findSpecifyPet(name: String, ownerId: String): Pet {
        val petId = "$ownerId-$name"
        return unitOfWork.petRepository.find(petId) ?: throw Exception("Pet not found")
    }

    override fun findAllPets() : List<Pet> {
        return unitOfWork.petRepository.findAll()
    }
}