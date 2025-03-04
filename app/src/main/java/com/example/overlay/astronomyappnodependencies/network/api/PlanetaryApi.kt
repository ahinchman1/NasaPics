package com.example.overlay.astronomyappnodependencies.network.api

import com.example.overlay.astronomyappnodependencies.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PlanetaryApi {


    @GET("planetary/apod?count=20")
    suspend fun getPictures(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<List<AstronomyPicture>>

    companion object {

        private val moshi: Moshi = Moshi.Builder()
            .add(LocalDateAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.nasa.gov")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        val instance: PlanetaryApi by lazy { retrofit.create(PlanetaryApi::class.java) }
    }
}