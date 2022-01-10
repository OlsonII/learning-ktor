package com.example.infrastructure.base

import com.example.domain.repositories.IDoctorRepository
import com.example.domain.repositories.IMedicalProcedureRepository
import com.example.domain.repositories.IOwnerRepository
import com.example.domain.repositories.IPetRepository
import com.example.infrastructure.repositories.DoctorRepository
import com.example.infrastructure.repositories.MedicalProcedureRepository
import com.example.infrastructure.repositories.OwnerRepository
import com.example.infrastructure.repositories.PetRepository
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection

class UnitOfWork(
    database: MongoDatabase
) {
    val doctorRepository: IDoctorRepository = DoctorRepository(database.getCollection())
    val ownerRepository: IOwnerRepository = OwnerRepository(database.getCollection())
    val petRepository: IPetRepository = PetRepository(database.getCollection())
    val medicalProcedureRepository: IMedicalProcedureRepository = MedicalProcedureRepository(database.getCollection())
}