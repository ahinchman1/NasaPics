package com.example.overlay.astronomyappnodependencies.network

import com.example.overlay.astronomyappnodependencies.network.api.AstronomyPicture
import com.example.overlay.astronomyappnodependencies.network.api.PlanetaryApi
import com.example.overlay.astronomyappnodependencies.util.Result
import retrofit2.HttpException
import java.util.logging.Level
import java.util.logging.Logger

interface NetworkDataSource {
    suspend fun loadAstronomyPhotos(): Result<List<AstronomyPicture>>
}

class AstronomyNetworkDataSource(
    private val apiClient: PlanetaryApi = PlanetaryApi.instance,
    private val logger: Logger = Logger.getGlobal(),
): NetworkDataSource {

    override suspend fun loadAstronomyPhotos(): Result<List<AstronomyPicture>> {
        return try {
            val response = apiClient.getPictures()
            if (response.isSuccessful) {
                when {
                    response.body() != null -> Result.Success(response.body() as List<AstronomyPicture>)
                    response.errorBody() != null -> {
                        logger.log(Level.SEVERE, "Error: ${response.errorBody()}")
                        Result.Failure(HttpException(response))
                    }
                    else -> {
                        logger.log(Level.SEVERE, "Parsing error. Cause of error: ${response.message()}")
                        Result.Failure(HttpException(response))
                    }
                }
            } else {
                logger.log(Level.SEVERE, "Response call not successful.")
                Result.Failure(HttpException(response))
            }
        } catch(e: Exception) {
            Result.Failure(e)
        }
    }
}
