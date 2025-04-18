package com.apil.charactergenerator.data.repository

import com.apil.charactergenerator.data.api.CharacterGeneratorApi
import com.apil.charactergenerator.data.model.character.CharacterGenerationRequest
import com.apil.charactergenerator.domain.model.Character
import com.apil.charactergenerator.domain.repository.CharacterRepository
import com.apil.charactergenerator.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: CharacterGeneratorApi
) : CharacterRepository {

    override suspend fun generateCharacter(
        nameHint: String?,
        minAge: Int?,
        maxAge: Int?,
        professionHint: String?,
        backgroundStoryHint: String?
    ): Result<Character> {
        return safeApiCall {
            val request = CharacterGenerationRequest(
                nameHint = nameHint,
                minAge = minAge,
                maxAge = maxAge,
                professionHint = professionHint,
                backgroundStoryHint = backgroundStoryHint
            )
            val response = api.generateCharacter(request)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    Character(
                        id = dto.id,
                        name = dto.name,
                        age = dto.age,
                        profession = dto.profession,
                        backgroundStory = dto.backgroundStory,
                        generationSeed = dto.generationSeed,
                        createdBy = dto.createdBy,
                        createdAt = dto.createdAt
                    )
                } ?: throw Exception("Empty response body")
            } else {
                throw Exception("Failed to generate character: ${response.errorBody()?.string()}")
            }
        }
    }

    override suspend fun getCharacterBySeed(seed: String): Result<Character> {
        return safeApiCall {
            val response = api.getCharacterBySeed(seed)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    Character(
                        id = dto.id,
                        name = dto.name,
                        age = dto.age,
                        profession = dto.profession,
                        backgroundStory = dto.backgroundStory,
                        generationSeed = dto.generationSeed,
                        createdBy = dto.createdBy,
                        createdAt = dto.createdAt
                    )
                } ?: throw Exception("Empty response body")
            } else {
                throw Exception("Failed to get character: ${response.errorBody()?.string()}")
            }
        }
    }

    override suspend fun getAllCharacters(page: Int, size: Int): Result<List<Character>> {
        return safeApiCall {
            val response = api.getAllCharacters(page, size)
            if (response.isSuccessful) {
                response.body()?.content?.map { dto ->
                    Character(
                        id = dto.id,
                        name = dto.name,
                        age = dto.age,
                        profession = dto.profession,
                        backgroundStory = dto.backgroundStory,
                        generationSeed = dto.generationSeed,
                        createdBy = dto.createdBy,
                        createdAt = dto.createdAt
                    )
                } ?: emptyList()
            } else {
                throw Exception("Failed to get characters: ${response.errorBody()?.string()}")
            }
        }
    }

    override fun getCharactersFlow(page: Int, size: Int): Flow<Result<List<Character>>> = flow {
        emit(getAllCharacters(page, size))
    }
}
