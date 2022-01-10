package com.example.infrastructure.repositories

import com.example.domain.entities.MedicalProcedure
import com.example.domain.repositories.IMedicalProcedureRepository
import com.example.infrastructure.base.GenericRepository
import com.mongodb.client.MongoCollection

class MedicalProcedureRepository(mongoCollection: MongoCollection<MedicalProcedure>)
    : GenericRepository<MedicalProcedure>(mongoCollection), IMedicalProcedureRepository {
}