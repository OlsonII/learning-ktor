package com.example.domain.repositories

interface IGenericRepository<T> {
    fun register(entity: T)
    fun update(entity: T)
    fun deactivate(id: String)
    fun find(id: String) : T?
    fun findAll() : List<T>
}