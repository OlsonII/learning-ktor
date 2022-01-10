package com.example.domain.entities

import com.example.domain.base.Entity
import com.example.domain.enums.MedicalProcedureState
import com.example.domain.enums.MedicalProcedureType
import java.time.LocalDate
import java.util.*

class MedicalProcedure(val pet: Pet, val doctorId: String, var date: Date, val type: MedicalProcedureType)
    : Entity(UUID.randomUUID().toString()) {

    var state: MedicalProcedureState = MedicalProcedureState.PROGRAMMED
        private set

    fun cancel(){
        state = MedicalProcedureState.CANCELED
    }

    fun reprogram(newDate: Date){
        date = newDate
        state = MedicalProcedureState.PROGRAMMED
    }

    fun complete(){
        state = MedicalProcedureState.SUCCESS
    }
}