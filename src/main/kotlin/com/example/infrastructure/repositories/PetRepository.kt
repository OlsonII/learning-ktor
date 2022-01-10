package com.example.infrastructure.repositories

import com.example.domain.entities.Pet
import com.example.domain.repositories.IPetRepository
import com.example.infrastructure.base.GenericRepository
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq

class PetRepository(mongoCollection: MongoCollection<Pet>) : GenericRepository<Pet>(mongoCollection), IPetRepository {
    override fun findAllPetsByOwner(ownerId: String): FindIterable<Pet> {
        return collection.find(Pet::ownerId eq ownerId)
    }
}