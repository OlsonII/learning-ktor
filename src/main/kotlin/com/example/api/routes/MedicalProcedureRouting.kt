package com.example.api.routes

import com.example.domain.services.IMedicalProcedureServices
import com.example.domain.services.requests.RegisterMedicalProcedureRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.time.Instant
import java.time.LocalDate
import java.util.*

fun Application.configureMedicalProcedureRouting(medicalProcedureServices: IMedicalProcedureServices) {
    routing {
        authenticate("auth-basic"){
            route("/medical-procedures"){
                get {
                    try {
                        val medicalProcedures = medicalProcedureServices.findAllMedicalProcedures()

                        call.respond(medicalProcedures)
                    }catch (exception: Exception){
                        call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
                post {
                    try {
                        val registerMedicalProcedureRequest = call.receive<RegisterMedicalProcedureRequest>()
                        val registeredMedicalProcedure =
                            medicalProcedureServices.registerMedicalProcedure(registerMedicalProcedureRequest)

                        call.respond(registeredMedicalProcedure)
                    }catch (exception: Exception){
                        call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
                put("/{code}/postpone"){
                    try {
                        val code = call.parameters["code"] ?: ""
                        val date = if (call.request.queryParameters["date"] != "") call.request.queryParameters["date"]
                        else throw Exception("Date is not valid")
                        val localDate = Date.from(Instant.parse(date))
                        val medicalProcedure = medicalProcedureServices.postponeMedicalProcedure(code, localDate)

                        call.respond(medicalProcedure)
                    }catch (exception: Exception){
                        call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
                delete("/{code}/cancel"){
                    try {
                        val code = call.parameters["code"] ?: ""
                        val medicalProcedure = medicalProcedureServices.cancelMedicalProcedure(code)

                        call.respond(medicalProcedure)
                    }catch (exception: Exception){
                        call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
                put("/{code}/complete"){
                    try {
                        val code = call.parameters["code"] ?: ""
                        val medicalProcedure = medicalProcedureServices.completeMedicalProcedure(code)

                        call.respond(medicalProcedure)
                    }catch (exception: Exception){
                        call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
            }

        }
    }
}