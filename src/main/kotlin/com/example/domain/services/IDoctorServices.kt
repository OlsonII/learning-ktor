package com.example.domain.services

import com.example.domain.entities.Doctor
import com.example.domain.services.requests.RegisterDoctorRequest
import com.example.domain.services.requests.UpdateDoctorRequest

interface IDoctorServices {
    fun registerDoctor(request: RegisterDoctorRequest) : Doctor
    fun updateDoctor(id: String, request: UpdateDoctorRequest) : Doctor
    fun findSpecifyDoctorById(id: String) : Doctor
    fun findAllDoctors() : List<Doctor>
}