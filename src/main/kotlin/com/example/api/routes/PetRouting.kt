package com.example.api.routes

import com.example.domain.services.IPetServices
import com.example.domain.services.requests.RegisterPetRequest
import com.example.domain.services.requests.UpdatePetRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configurePetRouting(petServices: IPetServices) {
    routing {
        authenticate("auth-basic"){
            route("owners/{ownerId}/pets"){
                get {
                    val pets = petServices.findAllPets()
                    call.respond(pets)
                }
                get("/{petName}"){
                    val ownerId = call.parameters["ownerId"] ?: ""
                    val petName = call.parameters["petName"] ?: ""
                    val pet = petServices.findSpecifyPet(petName, ownerId)
                    call.respond(pet)
                }
                post {
                    val registerPetRequest = call.receive<RegisterPetRequest>()
                    val registeredPet = petServices.registerPet(registerPetRequest)
                    call.respond(registeredPet)
                }
                put( "/{petName}") {
                    val ownerId = call.parameters["ownerId"] ?: ""
                    val petName = call.parameters["petName"] ?: ""
                    val updatePetRequest = call.receive<UpdatePetRequest>()
                    val updatedPet = petServices.updatePet(petName, ownerId, updatePetRequest)
                    call.respond(updatedPet)
                }
            }
        }
    }
}