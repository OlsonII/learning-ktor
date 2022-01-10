package com.example.api.routes;

import com.example.api.plugins.configureApiUtils
import com.example.api.plugins.configureAuthentication
import com.example.application.DoctorServices
import com.example.application.MedicalProcedureServices
import com.example.application.OwnerServices
import com.example.application.PetServices
import com.example.domain.entities.Doctor
import com.example.domain.entities.Owner
import com.example.domain.entities.Pet
import com.example.domain.enums.DoctorSpecialization
import com.example.domain.enums.MedicalProcedureType
import com.example.domain.enums.PetSize
import com.example.domain.services.IDoctorServices
import com.example.domain.services.IMedicalProcedureServices
import com.example.domain.services.IOwnerServices
import com.example.domain.services.IPetServices
import com.example.domain.services.requests.*
import com.example.infrastructure.base.UnitOfWork
import com.google.gson.Gson
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.testing.*
import org.litote.kmongo.KMongo
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

public class MedicalProcedureRoutingKtTest {

    private val connectionString = "mongodb://localhost:27017"
    private val client = KMongo.createClient(connectionString)
    private val database: MongoDatabase = client.getDatabase("VeterinaryServices")
    private val unitOfWork: UnitOfWork = UnitOfWork(database)

    // Services
    private val doctorServices: IDoctorServices = DoctorServices(unitOfWork)
    private val ownerServices: IOwnerServices = OwnerServices(unitOfWork)
    private val petServices: IPetServices = PetServices(unitOfWork)
    private val medicalProcedureServices: IMedicalProcedureServices = MedicalProcedureServices(unitOfWork)

    @Test
    fun getMedicalProceduresTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
            configurePetRouting(petServices)
            configureDoctorRouting(doctorServices)
            configureMedicalProcedureRouting(medicalProcedureServices)
        }) {
            with(handleRequest(HttpMethod.Get, "/medical-procedures") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun registerMedicalProcedure() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
            configurePetRouting(petServices)
            configureDoctorRouting(doctorServices)
            configureMedicalProcedureRouting(medicalProcedureServices)
        }) {
            registerDoctorCorrectTest("74937634832")
            registerOwnerCorrectTest("9479373")
            registerPetCorrectTest("Juancho", "9479373")
            with(handleRequest(HttpMethod.Post, "/medical-procedures") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
                addHeader(HttpHeaders.ContentType, "application/json")
                val request = RegisterMedicalProcedureRequest(
                    "Juancho",
                    "9479373",
                    Date(),
                    "74937634832",
                    MedicalProcedureType.MEDICAL_APPOINTMENT
                )
                val jsonRequest = Gson().toJson(request)
                setBody(jsonRequest)
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            database.getCollection<Doctor>().deleteOne(Doctor::id eq "74937634832")
            database.getCollection<Owner>().deleteOne(Owner::id eq "9479373")
            database.getCollection<Pet>().deleteOne(Pet::id eq "9479373-Juancho")
        }
    }

    private fun TestApplicationEngine.registerDoctorCorrectTest(id: String) {
        with(handleRequest(HttpMethod.Post, "/doctors") {
            addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            addHeader(HttpHeaders.ContentType, "application/json")
            val request = RegisterDoctorRequest(
                id,
                "Example name",
                "Example second name",
                "3114332454",
                "example address",
                DoctorSpecialization.GENERAL
            )
            val jsonRequest = Gson().toJson(request)
            setBody(jsonRequest)
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    private fun TestApplicationEngine.registerPetCorrectTest(name: String, ownerId: String) {
        with(handleRequest(HttpMethod.Post, "/owners/124562342/pets") {
            addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            addHeader(HttpHeaders.ContentType, "application/json")
            val request = RegisterPetRequest(
                name,
                ownerId,
                10.5f,
                "example address",
                PetSize.SMALL
            )
            val jsonRequest = Gson().toJson(request)
            setBody(jsonRequest)
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    private fun TestApplicationEngine.registerOwnerCorrectTest(id: String) {
        with(handleRequest(HttpMethod.Post, "/owners") {
            addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            addHeader(HttpHeaders.ContentType, "application/json")
            val request = RegisterOwnerRequest(
                id,
                "Example name",
                "Example second name",
                "3114332454",
                "example address"
            )
            val jsonRequest = Gson().toJson(request)
            setBody(jsonRequest)
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }
}