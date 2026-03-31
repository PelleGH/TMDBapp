package com.example.tmdbapp.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.themoviedb.org/"
private const val TMDB_BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwNDhiMDNkYTM3ODJhY2QxMTRkZGM2NTFhYTZjOTI2OCIsIm5iZiI6MTc3NDgxMTI0Ni4yMjU5OTk4LCJzdWIiOiI2OWM5Nzg2ZTJiYzUwYzJmNmQ0NGU1MTMiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.S8mEshHj66IUqU7BEhV5mzr3KgDD1EKa85y_ZLQFhAE"

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $TMDB_BEARER_TOKEN")
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