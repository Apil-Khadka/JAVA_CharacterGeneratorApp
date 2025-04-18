package com.apil.charactergenerator.domain.model

import java.util.UUID

data class Character(
    val id: UUID,
    val name: String,
    val age: Int,
    val profession: String,
    val backgroundStory: String,
    val generationSeed: String,
    val createdBy: String,
    val createdAt: String
)
