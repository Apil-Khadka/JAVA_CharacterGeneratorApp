package com.apil.charactergenerator.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            Result.success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> Result.failure(Exception("Network error: Please check your internet connection"))
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = throwable.response()?.errorBody()?.string()
                    Result.failure(Exception("Error $code: $errorResponse"))
                }
                else -> Result.failure(Exception(throwable.message ?: "Unknown error occurred"))
            }
        }
    }
}
