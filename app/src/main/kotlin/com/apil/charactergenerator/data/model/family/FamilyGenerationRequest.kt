package com.apil.charactergenerator.data.model.family

data class FamilyGenerationRequest(
    val familyName: String? = null,
    val memberCount: Int = 3,
    val backgroundHint: String? = null
)
