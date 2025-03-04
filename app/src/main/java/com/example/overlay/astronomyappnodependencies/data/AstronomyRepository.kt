package com.example.overlay.astronomyappnodependencies.data

import com.example.overlay.astronomyappnodependencies.util.Result
import com.example.overlay.astronomyappnodependencies.network.api.AstronomyPicture
import com.example.overlay.astronomyappnodependencies.network.AstronomyNetworkDataSource
import com.example.overlay.astronomyappnodependencies.network.NetworkDataSource
import java.util.logging.Level
import java.util.logging.Logger

interface AstronomyRepository {
    suspend fun retrieveAstronomyPictures(): Result<List<AstronomyPicture>>
}

class AstronomyRepositoryImpl(
    private val networkDataSource: NetworkDataSource = AstronomyNetworkDataSource(),
    private val logger: Logger = Logger.getGlobal(),
): AstronomyRepository {

    override suspend fun retrieveAstronomyPictures(): Result<List<AstronomyPicture>> {
        return when (val astronomyItem = networkDataSource.loadAstronomyPhotos()) {
            is Result.Success -> {
                val imageOnlyList = astronomyItem.data.filter { it.mediaType == "image" }
                Result.Success(imageOnlyList)
            }
            is Result.Failure -> {
                logger.log(Level.SEVERE, "Unable to retrieve astronomyItem from network. Error: ${astronomyItem.error}")
                Result.Failure(astronomyItem.error)
            }
        }
    }
}