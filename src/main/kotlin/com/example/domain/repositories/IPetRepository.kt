package com.example.domain.repositories

import com.example.domain.entities.Pet
import com.mongodb.client.FindIterable

interface IPetRepository : IGenericRepository<Pet> {
    fun findAllPetsByOwner(ownerId: String) : FindIterable<Pet>
}