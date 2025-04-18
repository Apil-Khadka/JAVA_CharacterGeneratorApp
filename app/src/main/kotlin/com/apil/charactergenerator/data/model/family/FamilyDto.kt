package com.apil.charactergenerator.data.model.family

import com.apil.charactergenerator.data.model.character.CharacterDto
import java.util.UUID

data class FamilyDto(
    val id: UUID,
    val name: String,
    val members: List<CharacterDto>,
    val backgroundStory: String,
    val createdBy: String,
    val createdAt: String
)
