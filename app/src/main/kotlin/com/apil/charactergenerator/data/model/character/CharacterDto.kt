package com.apil.charactergenerator.data.model.character

import java.util.UUID

data class CharacterDto(
    val id: UUID,
    val name: String,
    val age: Int,
    val profession: String,
    val backgroundStory: String,
    val generationSeed: String,
    val createdBy: String,
    val createdAt: String
)
