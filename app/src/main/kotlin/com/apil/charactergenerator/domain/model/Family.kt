package com.apil.charactergenerator.domain.model

import java.util.UUID

data class Family(
    val id: UUID,
    val name: String,
    val members: List<Character>,
    val backgroundStory: String,
    val createdBy: String,
    val createdAt: String
)
