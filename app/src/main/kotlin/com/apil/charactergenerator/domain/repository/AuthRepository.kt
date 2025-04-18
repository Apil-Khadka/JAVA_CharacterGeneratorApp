package com.apil.charactergenerator.domain.repository

import com.apil.charactergenerator.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun register(username: String, password: String): Result<User>
    fun logout()
    fun isLoggedIn(): Boolean
}
