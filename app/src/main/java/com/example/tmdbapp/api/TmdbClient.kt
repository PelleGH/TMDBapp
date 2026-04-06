package com.example.tmdbapp.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.tmdbapp.BuildConfig

private const val BASE_URL = "https://api.themoviedb.org/"
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_BEARER_TOKEN}")
            .addHeader("accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}

object TmdbClient {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    val api: TmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }
}