package com.apil.charactergenerator.data.api

import com.apil.charactergenerator.data.model.auth.AuthRequest
import com.apil.charactergenerator.data.model.auth.AuthResponse
import com.apil.charactergenerator.data.model.auth.RegisterRequest
import com.apil.charactergenerator.data.model.character.CharacterDto
import com.apil.charactergenerator.data.model.character.CharacterGenerationRequest
import com.apil.charactergenerator.data.model.common.Page
import com.apil.charactergenerator.data.model.family.FamilyDto
import com.apil.charactergenerator.data.model.family.FamilyGenerationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface CharacterGeneratorApi {
    // Authentication
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
    
    // Characters
    @POST("api/characters/generate")
    suspend fun generateCharacter(@Body request: CharacterGenerationRequest): Response<CharacterDto>
    
    @GET("api/characters/seed/{seed}")
    suspend fun getCharacterBySeed(@Path("seed") seed: String): Response<CharacterDto>
    
    @GET("api/characters")
    suspend fun getAllCharacters(
        @Query("page") page: Int = 0, 
        @Query("size") size: Int = 10
    ): Response<Page<CharacterDto>>
    
    // Families
    @POST("api/families/generate")
    suspend fun generateFamily(@Body request: FamilyGenerationRequest): Response<FamilyDto>
    
    @GET("api/families/{id}")
    suspend fun getFamilyById(@Path("id") id: UUID): Response<FamilyDto>
    
    @GET("api/families")
    suspend fun getAllFamilies(
        @Query("page") page: Int = 0, 
        @Query("size") size: Int = 10
    ): Response<Page<FamilyDto>>
}
