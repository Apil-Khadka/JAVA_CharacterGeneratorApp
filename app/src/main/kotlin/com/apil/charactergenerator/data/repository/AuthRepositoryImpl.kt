package com.apil.charactergenerator.data.repository

import com.apil.charactergenerator.data.api.CharacterGeneratorApi
import com.apil.charactergenerator.data.model.auth.AuthRequest
import com.apil.charactergenerator.data.model.auth.RegisterRequest
import com.apil.charactergenerator.domain.model.User
import com.apil.charactergenerator.domain.repository.AuthRepository
import com.apil.charactergenerator.utils.TokenManager
import com.apil.charactergenerator.utils.safeApiCall
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: CharacterGeneratorApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        return safeApiCall {
            val response = api.login(AuthRequest(username, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    tokenManager.saveToken(authResponse.token)
                    User(authResponse.username, authResponse.token)
                } ?: throw Exception("Empty response body")
            } else {
                throw Exception("Login failed: ${response.errorBody()?.string()}")
            }
        }
    }

    override suspend fun register(username: String, password: String): Result<User> {
        return safeApiCall {
            val response = api.register(RegisterRequest(username, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    tokenManager.saveToken(authResponse.token)
                    User(authResponse.username, authResponse.token)
                } ?: throw Exception("Empty response body")
            } else {
                throw Exception("Registration failed: ${response.errorBody()?.string()}")
            }
        }
    }

    override fun logout() {
        tokenManager.clearToken()
    }

    override fun isLoggedIn(): Boolean {
        return tokenManager.getToken() != null
    }
}
