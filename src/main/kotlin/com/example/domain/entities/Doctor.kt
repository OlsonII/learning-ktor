package com.example.domain.entities

import com.example.domain.base.Entity
import com.example.domain.enums.DoctorSpecialization
import com.example.domain.enums.DoctorStatus

class Doctor(
    id: String, var firstName: String,
    var secondName: String, var address: String,
    var phone: String, var specialization: DoctorSpecialization)
    : Entity(id) {

    private var _status: DoctorStatus = DoctorStatus.AVAILABLE

    var status: DoctorStatus
        get() = _status
        set(value) {
            _status = value
        }



}