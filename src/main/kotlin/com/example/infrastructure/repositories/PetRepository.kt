package com.example.infrastructure.repositories

import com.example.domain.entities.Pet
import com.example.domain.repositories.IPetRepository
import com.example.infrastructure.base.GenericRepository
import com.mongodb.client.MongoCollection

class PetRepository(mongoCollection: MongoCollection<Pet>) : GenericRepository<Pet>(mongoCollection), IPetRepository {
}