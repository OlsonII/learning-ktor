package com.example.application

import com.example.domain.entities.MedicalProcedure
import com.example.domain.services.IMedicalProcedureServices
import com.example.domain.services.requests.RegisterMedicalProcedureRequest
import com.example.infrastructure.base.UnitOfWork
import java.time.LocalDate
import java.util.*

class MedicalProcedureServices(private val unitOfWork: UnitOfWork) : IMedicalProcedureServices {

    override fun registerMedicalProcedure(request: RegisterMedicalProcedureRequest): MedicalProcedure {
        val petId = "${request.ownerId}-${request.petName}"
        val pet = unitOfWork.petRepository.find(petId) ?: throw Exception("Pet not found")
        val doctor = unitOfWork.doctorRepository.find(request.doctorId) ?: throw Exception("Doctor not found")
        val medicalProcedure = MedicalProcedure(pet, doctor.id, request.date, request.medicalProcedureType)

        unitOfWork.medicalProcedureRepository.register(medicalProcedure)
        return medicalProcedure
    }

    override fun postponeMedicalProcedure(code: String, date: Date): MedicalProcedure {
        val medicalProcedure = unitOfWork.medicalProcedureRepository.find(code) ?: throw Exception("Medical Procedure not found")
        medicalProcedure.reprogram(date)

        unitOfWork.medicalProcedureRepository.update(medicalProcedure)
        return medicalProcedure
    }

    override fun cancelMedicalProcedure(code: String): MedicalProcedure {
        val medicalProcedure = unitOfWork.medicalProcedureRepository.find(code) ?: throw Exception("Medical Procedure not found")
        medicalProcedure.cancel()

        unitOfWork.medicalProcedureRepository.update(medicalProcedure)
        return medicalProcedure
    }

    override fun findAllMedicalProcedures(): List<MedicalProcedure> {
        return unitOfWork.medicalProcedureRepository.findAll()
    }

    override fun completeMedicalProcedure(code: String): MedicalProcedure {
        val medicalProcedure = unitOfWork.medicalProcedureRepository.find(code) ?: throw Exception("Medical Procedure not found")
        medicalProcedure.complete()

        unitOfWork.medicalProcedureRepository.update(medicalProcedure)
        return medicalProcedure
    }
}