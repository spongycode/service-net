package com.spongycode.servicenet.data

import com.spongycode.servicenet.data.model.ApiTime
import com.spongycode.servicenet.data.model.Quote
import com.spongycode.servicenet.data.model.University
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun getUniversitiesList(@Query("country") country: String): List<University>

    @GET("Asia/Kolkata")
    suspend fun getCurrentTime(): ApiTime

    @GET("random")
    suspend fun getRandomQuote(): Quote


    companion object {
        private const val BASE_URL_UNIVERSITY = "http://universities.hipolabs.com/"
        private const val BASE_URL_TIME = "https://worldtimeapi.org/api/timezone/"
        private const val BASE_URL_QUOTE = "https://api.quotable.io/"

        fun create(type: String): ApiService {
            val url = when (type) {
                BaseUrls.BASE_URL_UNIVERSITY.toString() -> BASE_URL_UNIVERSITY
                BaseUrls.BASE_URL_QUOTE.toString() -> BASE_URL_QUOTE
                BaseUrls.BASE_URL_TIME.toString() -> BASE_URL_TIME
                else -> "NONE"
            }
            val retrofit = Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

    enum class BaseUrls {
        BASE_URL_UNIVERSITY, BASE_URL_TIME, BASE_URL_QUOTE
    }
}