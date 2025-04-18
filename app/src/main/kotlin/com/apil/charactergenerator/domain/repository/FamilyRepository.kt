package com.apil.charactergenerator.domain.repository

import com.apil.charactergenerator.domain.model.Family
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface FamilyRepository {
    suspend fun generateFamily(
        familyName: String? = null,
        memberCount: Int = 3,
        backgroundHint: String? = null
    ): Result<Family>
    
    suspend fun getFamilyById(id: UUID): Result<Family>
    
    suspend fun getAllFamilies(page: Int, size: Int): Result<List<Family>>
    
    fun getFamiliesFlow(page: Int, size: Int): Flow<Result<List<Family>>>
}
