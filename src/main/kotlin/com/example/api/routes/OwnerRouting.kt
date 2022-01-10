package com.example.api.routes

import com.example.domain.services.IOwnerServices
import com.example.domain.services.requests.RegisterOwnerRequest
import com.example.domain.services.requests.UpdateOwnerRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureOwnerRouting(ownerServices: IOwnerServices) {
    routing {
        authenticate("auth-basic"){
            route("/owners"){
                get {
                    val owners = ownerServices.findAllOwners()
                    call.respond(owners)
                }
                get("/{ownerId}"){
                    try {
                        val ownerId = call.parameters["ownerId"] ?: ""
                        val owner = ownerServices.findSpecifyOwnerById(ownerId)
                        call.respond(owner)
                    }catch (exception: Exception){
                        if (exception.message == "Owner not found")
                            call.respond(status = HttpStatusCode.NotFound, exception.message.toString())
                        else
                            call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
                put("/{ownerId}"){
                    try {
                        val ownerId = call.parameters["ownerId"] ?: ""
                        val updateOwnerRequest = call.receive<UpdateOwnerRequest>()
                        val updatedOwner = ownerServices.updateOwner(ownerId, updateOwnerRequest)
                        call.respond(updatedOwner)
                    }catch (exception: Exception){
                        if (exception.message == "Owner not found")
                            call.respond(status = HttpStatusCode.NotFound, exception.message.toString())
                        else
                            call.respond(status = HttpStatusCode.InternalServerError, "Error in request")
                    }
                }
                post {
                    val registerOwnerRequest = call.receive<RegisterOwnerRequest>()
                    val registeredOwner = ownerServices.registerOwner(registerOwnerRequest)
                    call.respond(registeredOwner)
                }
            }
        }
    }
}