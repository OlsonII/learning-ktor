package com.example.api.routes

import com.example.api.plugins.configureApiUtils
import com.example.api.plugins.configureAuthentication
import com.example.application.DoctorServices
import com.example.domain.entities.Doctor
import com.example.domain.enums.DoctorSpecialization
import com.example.domain.services.IDoctorServices
import com.example.domain.services.requests.RegisterDoctorRequest
import com.example.domain.services.requests.UpdateDoctorRequest
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

class DoctorRoutingTests {

    private val connectionString = "mongodb://localhost:27017"
    private val client = KMongo.createClient(connectionString)
    private val database: MongoDatabase = client.getDatabase("VeterinaryServices")
    private val unitOfWork: UnitOfWork = UnitOfWork(database)

    // Services
    private val doctorServices: IDoctorServices = DoctorServices(unitOfWork)

    @Test
    fun getDoctorsCorrectTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureDoctorRouting(doctorServices)
        }) {
            with(handleRequest(HttpMethod.Get, "/doctors") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun getDoctorsBadTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureDoctorRouting(doctorServices)
        }) {
            with(handleRequest(HttpMethod.Get, "/doctors") {
            }) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun registerDoctorCorrectTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureDoctorRouting(doctorServices)
        }) {
            registerDoctorCorrectTest("1065843320")
            database.getCollection<Doctor>().deleteOne(Doctor::id eq "1065843320")
        }
    }

    @Test
    fun getSpecifyDoctorCorrectTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureDoctorRouting(doctorServices)
        }) {
            registerDoctorCorrectTest("1065843321")

            with(handleRequest(HttpMethod.Get, "/doctors/1065843321") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            database.getCollection<Doctor>().deleteOne(Doctor::id eq "1065843321")
        }
    }

    @Test
    fun updateDoctorCorrectTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureDoctorRouting(doctorServices)
        }) {
            registerDoctorCorrectTest("1065843327")
            with(handleRequest(HttpMethod.Put, "/doctors/1065843327") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
                addHeader(HttpHeaders.ContentType, "application/json")
                val request = UpdateDoctorRequest(
                    "Example name 2",
                    "Example second name 2",
                    "3114332454",
                    "example address",
                    DoctorSpecialization.SURGEON
                )
                val jsonRequest = Gson().toJson(request)
                setBody(jsonRequest)
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            database.getCollection<Doctor>().deleteOne(Doctor::id eq "1065843327")
        }
    }

    @Test
    fun getSpecifyDoctorFailedTest() {
        withTestApplication({
            configureApiUtils()
            configureAuthentication()
            configureDoctorRouting(doctorServices)
        }) {

            with(handleRequest(HttpMethod.Get, "/doctors/1065843322") {
                addHeader(HttpHeaders.Authorization, "basic YWRtaW46YWRtaW4=")
            }) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
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
}