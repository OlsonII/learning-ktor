package com.example.infrastructure.repositories

import com.example.domain.entities.Doctor
import com.example.domain.repositories.IDoctorRepository
import com.example.infrastructure.base.GenericRepository
import com.mongodb.client.MongoCollection

class DoctorRepository(mongoCollection: MongoCollection<Doctor>) : GenericRepository<Doctor>(mongoCollection), IDoctorRepository {
}