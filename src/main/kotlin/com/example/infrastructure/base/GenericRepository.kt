package com.example.infrastructure.base

import com.example.domain.base.Entity
import com.example.domain.repositories.IGenericRepository
import com.mongodb.client.MongoCollection
import org.litote.kmongo.*

abstract class GenericRepository<T>(mongoCollection: MongoCollection<T>) : IGenericRepository<T> where T : Entity {

    protected val collection: MongoCollection<T> = mongoCollection

    override fun register(entity: T){
        collection.insertOne(entity)
    }

    override fun update(entity: T) {
        collection.updateOne(Entity::id eq  entity.id, entity)
    }

    override fun find(id: String): T? {
        return collection.findOneById(id)
    }

    override fun deactivate(id: String) {
        collection.deleteOne(Entity::id eq id)
    }

    override fun findAll(): List<T> {
        return collection.find().toList()
    }

}