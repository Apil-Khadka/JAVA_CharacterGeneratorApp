package com.apil.charactergenerator.data.repository

import com.apil.charactergenerator.data.api.CharacterGeneratorApi
import com.apil.charactergenerator.data.model.family.FamilyGenerationRequest
import com.apil.charactergenerator.domain.model.Character
import com.apil.charactergenerator.domain.model.Family
import com.apil.charactergenerator.domain.repository.FamilyRepository
import com.apil.charactergenerator.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class FamilyRepositoryImpl @Inject constructor(
    private val api: CharacterGeneratorApi
) : FamilyRepository {

    override suspend fun generateFamily(
        familyName: String?,
        memberCount: Int,
        backgroundHint: String?
    ): Result<Family> {
        return safeApiCall {
            val request = FamilyGenerationRequest(
                familyName = familyName,
                memberCount = memberCount,
                backgroundHint = backgroundHint
            )
            val response = api.generateFamily(request)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    Family(
                        id = dto.id,
                        name = dto.name,
                        members = dto.members.map { characterDto ->
                            Character(
                                id = characterDto.id,
                                name = characterDto.name,
                                age = characterDto.age,
                                profession = characterDto.profession,
                                backgroundStory = characterDto.backgroundStory,
                                generationSeed = characterDto.generationSeed,
                                createdBy = characterDto.createdBy,
                                createdAt = characterDto.createdAt
                            )
                        },
                        backgroundStory = dto.backgroundStory,
                        createdBy = dto.createdBy,
                        createdAt = dto.createdAt
                    )
                } ?: throw Exception("Empty response body")
            } else {
                throw Exception("Failed to generate family: ${response.errorBody()?.string()}")
            }
        }
    }

    override suspend fun getFamilyById(id: UUID): Result<Family> {
        return safeApiCall {
            val response = api.getFamilyById(id)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    Family(
                        id = dto.id,
                        name = dto.name,
                        members = dto.members.map { characterDto ->
                            Character(
                                id = characterDto.id,
                                name = characterDto.name,
                                age = characterDto.age,
                                profession = characterDto.profession,
                                backgroundStory = characterDto.backgroundStory,
                                generationSeed = characterDto.generationSeed,
                                createdBy = characterDto.createdBy,
                                createdAt = characterDto.createdAt
                            )
                        },
                        backgroundStory = dto.backgroundStory,
                        createdBy = dto.createdBy,
                        createdAt = dto.createdAt
                    )
                } ?: throw Exception("Empty response body")
            } else {
                throw Exception("Failed to get family: ${response.errorBody()?.string()}")
            }
        }
    }

    override suspend fun getAllFamilies(page: Int, size: Int): Result<List<Family>> {
        return safeApiCall {
            val response = api.getAllFamilies(page, size)
            if (response.isSuccessful) {
                response.body()?.content?.map { dto ->
                    Family(
                        id = dto.id,
                        name = dto.name,
                        members = dto.members.map { characterDto ->
                            Character(
                                id = characterDto.id,
                                name = characterDto.name,
                                age = characterDto.age,
                                profession = characterDto.profession,
                                backgroundStory = characterDto.backgroundStory,
                                generationSeed = characterDto.generationSeed,
                                createdBy = characterDto.createdBy,
                                createdAt = characterDto.createdAt
                            )
                        },
                        backgroundStory = dto.backgroundStory,
                        createdBy = dto.createdBy,
                        createdAt = dto.createdAt
                    )
                } ?: emptyList()
            } else {
                throw Exception("Failed to get families: ${response.errorBody()?.string()}")
            }
        }
    }

    override fun getFamiliesFlow(page: Int, size: Int): Flow<Result<List<Family>>> = flow {
        emit(getAllFamilies(page, size))
    }
}
