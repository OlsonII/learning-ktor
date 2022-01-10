package com.example.api.routes

import com.example.api.plugins.configureApiUtils
import com.example.api.plugins.configureAuthentication
import com.example.application.OwnerServices
import com.example.application.PetServices
import com.example.domain.entities.Owner
import com.example.domain.entities.Pet
import com.example.domain.enums.PetSize
import com.example.domain.services.IOwnerServices
import com.example.domain.services.IPetServices
import com.example.domain.services.requests.RegisterOwnerRequest
import com.example.domain.services.requests.RegisterPetRequest
import com.example.infrastructure.base.UnitOfWork
import com.google.gson.Gson
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.testing.*
import org.litote.kmongo.KMongo
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import kotlin.test.Test
import kotlin.test.assertEquals

class PetRoutingTests {

    private val connectionString = "mongodb://localhost:27017"
    private val client = KMongo.createClient(connectionString)
    private val database: MongoDatabase = client.getDatabase("VeterinaryServices")
    private val unitOfWork: UnitOfWork = UnitOfWork(database)

    // Services
    private val ownerServices: IOwnerServices = OwnerServices(unitOfWork)
    private val petServices: IPetServices = PetServices(unitOfWork)

    @Test
    fun getOwnersPetsTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
            configurePetRouting(petServices)
        }) {
            registerOwnerCorrectTest("1067844900")
            with(handleRequest(HttpMethod.Get, "/owners/1067844900/pets") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            database.getCollection<Owner>().deleteOne(Owner::id eq "1067844900")
        }
    }

    @Test
    fun getOwnersPetTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
            configurePetRouting(petServices)
        }) {
            registerOwnerCorrectTest("1067844300")
            registerPetCorrectTest("Rosco", "1067844300")
            with(handleRequest(HttpMethod.Get, "/owners/1067844300/pets/Rosco") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            database.getCollection<Owner>().deleteOne(Owner::id eq "1067844300")
            database.getCollection<Pet>().deleteOne(Pet::id eq "1067844300-Rosco")
        }
    }

    @Test
    fun registerOwnersPetTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
            configurePetRouting(petServices)
        }) {
            registerOwnerCorrectTest("124562342")
            registerPetCorrectTest("Max", "124562342")

            database.getCollection<Owner>().deleteOne(Owner::id eq "124562342")
            database.getCollection<Pet>().deleteOne(Pet::id eq "124562342-Max")
        }
    }

    @Test
    fun updateOwnersPetTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
            configurePetRouting(petServices)
        }) {
            registerOwnerCorrectTest("124562367")
            registerPetCorrectTest("Rocky", "124562367")
            with(handleRequest(HttpMethod.Put, "/owners/124562367/pets/Rocky") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
                addHeader(HttpHeaders.ContentType, "application/json")
                val request = RegisterPetRequest(
                    "Rocky",
                    "124562367",
                    15.5f,
                    "example address",
                    PetSize.MEDIUM
                )
                val jsonRequest = Gson().toJson(request)
                setBody(jsonRequest)
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            database.getCollection<Owner>().deleteOne(Owner::id eq "124562367")
            database.getCollection<Pet>().deleteOne(Pet::id eq "124562367-Rocky")
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