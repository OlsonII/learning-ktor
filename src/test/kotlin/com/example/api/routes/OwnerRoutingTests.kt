package com.example.api.routes

import com.example.api.plugins.configureApiUtils
import com.example.api.plugins.configureAuthentication
import com.example.application.OwnerServices
import com.example.domain.entities.Owner
import com.example.domain.services.IOwnerServices
import com.example.domain.services.requests.RegisterOwnerRequest
import com.example.domain.services.requests.UpdateOwnerRequest
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

class OwnerRoutingTests {

    private val connectionString = "mongodb://localhost:27017"
    private val client = KMongo.createClient(connectionString)
    private val database: MongoDatabase = client.getDatabase("VeterinaryServices")
    private val unitOfWork: UnitOfWork = UnitOfWork(database)

    // Services
    private val ownerServices: IOwnerServices = OwnerServices(unitOfWork)

    @Test
    fun getOwnersCorrectTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
        }) {
            with(handleRequest(HttpMethod.Get, "/owners") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun getOwnersBadTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
        }) {
            with(handleRequest(HttpMethod.Get, "/owners") {
            }) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun registerOwnerCorrectTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
        }) {
            registerOwnerCorrectTest("1065843320")
            database.getCollection<Owner>().deleteOne(Owner::id eq "1065843320")
        }
    }

    @Test
    fun getSpecifyOwnerCorrectTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
        }) {
            registerOwnerCorrectTest("1065843321")

            with(handleRequest(HttpMethod.Get, "/owners/1065843321") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            database.getCollection<Owner>().deleteOne(Owner::id eq "1065843321")
        }
    }

    @Test
    fun updateOwnerCorrectTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
        }) {
            registerOwnerCorrectTest("1065843327")
            with(handleRequest(HttpMethod.Put, "/owners/1065843327") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
                addHeader(HttpHeaders.ContentType, "application/json")
                val request = UpdateOwnerRequest(
                    "Example name 2",
                    "Example second name 2",
                    "3114332454",
                    "example address"
                )
                val jsonRequest = Gson().toJson(request)
                setBody(jsonRequest)
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            database.getCollection<Owner>().deleteOne(Owner::id eq "1065843327")
        }
    }

    @Test
    fun getSpecifyOwnerFailedTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureOwnerRouting(ownerServices)
        }) {
            with(handleRequest(HttpMethod.Get, "/owners/1065843322") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            }) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
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