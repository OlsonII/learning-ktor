package com.example.api.routes

import com.example.domain.services.IDoctorServices
import com.example.domain.services.requests.RegisterDoctorRequest
import com.example.domain.services.requests.UpdateDoctorRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureDoctorRouting(doctorServices: IDoctorServices) {
    routing {
        authenticate("auth-basic"){
            route("/doctors"){
                get {
                    try {
                        val doctors = doctorServices.findAllDoctors()
                        call.respond(doctors)
                    }catch (exception: Exception){
                        call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
                get("/{doctorId}"){
                    try {
                        val doctorId = call.parameters["doctorId"] ?: ""
                        val doctor = doctorServices.findSpecifyDoctorById(doctorId)
                        call.respond(doctor)
                    }catch (exception: Exception){
                        if (exception.message == "Doctor not found")
                            call.respond(status = HttpStatusCode.NotFound, exception.message.toString())
                        else
                            call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
                put("/{doctorId}"){
                    try {
                        val doctorId = call.parameters["doctorId"] ?: throw Exception("Doctor not found")
                        val updateDoctorRequest = call.receive<UpdateDoctorRequest>()
                        val updatedDoctor = doctorServices.updateDoctor(doctorId, updateDoctorRequest)
                        call.respond(updatedDoctor)
                    }catch (exception: Exception){
                        println(exception.toString())
                        if (exception.message == "Doctor not found")
                            call.respond(status = HttpStatusCode.NotFound, exception.message.toString())
                        else
                            call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
                post {
                    try {
                        val registerDoctorRequest = call.receive<RegisterDoctorRequest>()
                        val registeredDoctor = doctorServices.registerDoctor(registerDoctorRequest)
                        call.respond(registeredDoctor)
                    }catch (exception: Exception){
                        call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
            }
        }
    }
}