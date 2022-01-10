package com.example.application

import com.example.domain.entities.Doctor
import com.example.domain.services.IDoctorServices
import com.example.domain.services.requests.RegisterDoctorRequest
import com.example.domain.services.requests.UpdateDoctorRequest
import com.example.infrastructure.base.UnitOfWork

class DoctorServices(private val unitOfWork: UnitOfWork) : IDoctorServices {

    override fun registerDoctor(request: RegisterDoctorRequest): Doctor {
        val doctor = Doctor(
            id = request.id, firstName = request.firstName,
            secondName = request.secondName, phone = request.phone,
            address = request.address, specialization = request.specialization)
        unitOfWork.doctorRepository.register(doctor)
        return doctor
    }

    override fun updateDoctor(id: String, request: UpdateDoctorRequest): Doctor {
        val doctor = unitOfWork.doctorRepository.find(id) ?: throw Exception("Doctor not found")
        doctor.firstName = request.firstName
        doctor.secondName = request.secondName
        doctor.address = request.address
        doctor.phone = request.phone
        doctor.specialization = request.specialization
        unitOfWork.doctorRepository.update(doctor)
        return doctor
    }

    override fun findSpecifyDoctorById(id: String): Doctor {
        return unitOfWork.doctorRepository.find(id) ?: throw Exception("Doctor not found")
    }

    override fun findAllDoctors(): List<Doctor> {
        return unitOfWork.doctorRepository.findAll()
    }
}