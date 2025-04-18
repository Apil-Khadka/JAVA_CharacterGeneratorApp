package com.apil.charactergenerator.domain.repository

import com.apil.charactergenerator.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun generateCharacter(
        nameHint: String? = null,
        minAge: Int? = null,
        maxAge: Int? = null,
        professionHint: String? = null,
        backgroundStoryHint: String? = null
    ): Result<Character>
    
    suspend fun getCharacterBySeed(seed: String): Result<Character>
    
    suspend fun getAllCharacters(page: Int, size: Int): Result<List<Character>>
    
    fun getCharactersFlow(page: Int, size: Int): Flow<Result<List<Character>>>
}
