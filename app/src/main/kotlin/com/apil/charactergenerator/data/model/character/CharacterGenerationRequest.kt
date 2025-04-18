package com.apil.charactergenerator.data.model.character

data class CharacterGenerationRequest(
    val nameHint: String? = null,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val professionHint: String? = null,
    val backgroundStoryHint: String? = null
)
