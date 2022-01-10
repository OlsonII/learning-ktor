package com.example.application

import com.example.domain.entities.Owner
import com.example.domain.services.IOwnerServices
import com.example.domain.services.requests.RegisterOwnerRequest
import com.example.domain.services.requests.UpdateOwnerRequest
import com.example.infrastructure.base.UnitOfWork

class OwnerServices(private val unitOfWork: UnitOfWork) : IOwnerServices {

    override fun registerOwner(request: RegisterOwnerRequest): Owner {
        val owner = Owner(
            id = request.id, firstName = request.firstName,
            secondName = request.secondName, phone = request.phone,
            address = request.address)
        unitOfWork.ownerRepository.register(owner)

        return owner
    }

    override fun updateOwner(id: String, request: UpdateOwnerRequest): Owner {
        val owner = unitOfWork.ownerRepository.find(id) ?: throw Exception("Owner not found")

        owner.firstName = if (request.firstName != "") request.firstName else owner.firstName
        owner.secondName = if (request.secondName != "") request.secondName else owner.secondName
        owner.address = if (request.address != "") request.address else owner.address
        owner.phone = if (request.phone != "") request.phone else owner.phone

        unitOfWork.ownerRepository.update(owner)
        return owner
    }

    override fun findSpecifyOwnerById(id: String): Owner {
        return unitOfWork.ownerRepository.find(id) ?: throw Exception("Owner not found")
    }

    override fun findAllOwners(): List<Owner> {
        return unitOfWork.ownerRepository.findAll()
    }

}