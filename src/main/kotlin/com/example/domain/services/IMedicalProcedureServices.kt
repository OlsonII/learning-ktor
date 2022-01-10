package com.example.domain.services

import com.example.domain.entities.MedicalProcedure
import com.example.domain.services.requests.RegisterMedicalProcedureRequest
import java.time.LocalDate
import java.util.*

interface IMedicalProcedureServices {
    fun registerMedicalProcedure(request: RegisterMedicalProcedureRequest) : MedicalProcedure
    fun postponeMedicalProcedure(code: String, date: Date) : MedicalProcedure
    fun cancelMedicalProcedure(code: String) : MedicalProcedure
    fun findAllMedicalProcedures() : List<MedicalProcedure>
    fun completeMedicalProcedure(code: String) : MedicalProcedure
}