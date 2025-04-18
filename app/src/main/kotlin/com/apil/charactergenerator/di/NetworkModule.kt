package com.apil.charactergenerator.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.apil.charactergenerator.data.api.CharacterGeneratorApi
import com.apil.charactergenerator.utils.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    private const val BASE_URL = "https://your-api-url.com/" // Replace with your actual API URL
    private const val GRAPHQL_URL = "$BASE_URL/graphql"
    
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenManager: TokenManager
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = tokenManager.getToken()
                val request = if (token != null) {
                    chain.request().newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Singleton
    @Provides
    fun provideCharacterGeneratorApi(retrofit: Retrofit): CharacterGeneratorApi {
        return retrofit.create(CharacterGeneratorApi::class.java)
    }
    
    @Singleton
    @Provides
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(GRAPHQL_URL)
            .okHttpClient(okHttpClient)
            .build()
    }
}
