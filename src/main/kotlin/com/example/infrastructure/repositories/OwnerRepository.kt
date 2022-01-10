package com.example.infrastructure.repositories

import com.example.domain.entities.Owner
import com.example.domain.repositories.IOwnerRepository
import com.example.infrastructure.base.GenericRepository
import com.mongodb.client.MongoCollection

class OwnerRepository(mongoCollection: MongoCollection<Owner>) : GenericRepository<Owner>(mongoCollection), IOwnerRepository {
}