package com.example.domain.base

import org.bson.codecs.pojo.annotations.BsonId

abstract class Entity(@BsonId var id: String) {}