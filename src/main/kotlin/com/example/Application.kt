package com.example

import com.example.api.routes.configureOwnerRouting
import com.example.api.routes.configurePetRouting
import com.example.api.plugins.configureApiUtils
import com.example.api.plugins.configureAuthentication
import com.example.api.routes.configureDoctorRouting
import com.example.api.routes.configureMedicalProcedureRouting
import com.example.application.DoctorServices
import com.example.application.MedicalProcedureServices
import com.example.application.OwnerServices
import com.example.application.PetServices
import com.example.domain.services.IDoctorServices
import com.example.domain.services.IMedicalProcedureServices
import com.example.domain.services.IOwnerServices
import com.example.domain.services.IPetServices
import com.example.infrastructure.base.UnitOfWork
import com.mongodb.client.MongoDatabase
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.KMongo

// Infrastructure
const val CONNECTION_STRING = "mongodb://localhost:27017"
val client = KMongo.createClient(CONNECTION_STRING)
val database: MongoDatabase = client.getDatabase("VeterinaryServices")
val unitOfWork: UnitOfWork = UnitOfWork(database)

// Services
val ownerServices: IOwnerServices = OwnerServices(unitOfWork)
val petServices: IPetServices = PetServices(unitOfWork)
val doctorServices: IDoctorServices = DoctorServices(unitOfWork)
val medicalProcedureServices: IMedicalProcedureServices = MedicalProcedureServices(unitOfWork)

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureAuthentication()
        configureApiUtils()
        configureOwnerRouting(ownerServices)
        configurePetRouting(petServices)
        configureDoctorRouting(doctorServices)
        configureMedicalProcedureRouting(medicalProcedureServices)
    }.start(wait = true)
}