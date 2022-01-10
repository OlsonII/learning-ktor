package com.example.domain.services.requests

import com.example.domain.enums.DoctorSpecialization

data class RegisterDoctorRequest(
    val id: String,
    val firstName: String,
    val secondName: String,
    val address: String,
    val phone: String,
    val specialization: DoctorSpecialization) {}