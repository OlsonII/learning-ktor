package com.example.domain.services

import com.example.domain.entities.Owner
import com.example.domain.services.requests.RegisterOwnerRequest
import com.example.domain.services.requests.UpdateOwnerRequest

interface IOwnerServices {
    fun registerOwner(request: RegisterOwnerRequest) : Owner
    fun updateOwner(id: String, request: UpdateOwnerRequest) : Owner
    fun findSpecifyOwnerById(id: String) : Owner
    fun findAllOwners() : List<Owner>
}