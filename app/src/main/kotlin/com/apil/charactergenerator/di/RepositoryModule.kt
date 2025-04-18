package com.apil.charactergenerator.di

import com.apil.charactergenerator.data.repository.AuthRepositoryImpl
import com.apil.charactergenerator.data.repository.CharacterRepositoryImpl
import com.apil.charactergenerator.data.repository.FamilyRepositoryImpl
import com.apil.charactergenerator.domain.repository.AuthRepository
import com.apil.charactergenerator.domain.repository.CharacterRepository
import com.apil.charactergenerator.domain.repository.FamilyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindCharacterRepository(
        characterRepositoryImpl: CharacterRepositoryImpl
    ): CharacterRepository
    
    @Binds
    @Singleton
    abstract fun bindFamilyRepository(
        familyRepositoryImpl: FamilyRepositoryImpl
    ): FamilyRepository
}
