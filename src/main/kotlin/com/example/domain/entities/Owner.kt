package com.example.domain.entities

import com.example.domain.base.Entity

class Owner(id: String, var firstName: String,
                 var secondName: String, var address: String,
                 var phone: String)
    : Entity(id) {}