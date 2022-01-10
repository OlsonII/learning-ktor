package com.example.domain.services.requests

import com.example.domain.enums.MedicalProcedureType
import java.time.LocalDate
import java.util.*

data class RegisterMedicalProcedureRequest(
    val petName: String,
    val ownerId: String,
    val date: Date,
    val doctorId: String,
    val medicalProcedureType: MedicalProcedureType) {}
