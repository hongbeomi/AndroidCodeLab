package com.github.hongbeomi.flickrcodelab.data.source.remote.connection

import android.content.Context
import com.github.hongbeomi.flickrcodelab.BuildConfig
import com.github.hongbeomi.flickrcodelab.R
import com.github.hongbeomi.flickrcodelab.model.remote.FlickrResponse
import com.github.hongbeomi.flickrcodelab.model.remote.PhotosResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


private const val NETWORK_TIMEOUT = 10000
private const val PER_PAGE = 60

class FlickrNetworkService(
    context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val defaultQuery =
        "&api_key=${context.getString(R.string.api_key)}&format=json&nojsoncallback=1&tags=soccer"
    private val format = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    suspend fun getSoccerPhotos(page: Int): PhotosResponse = withContext(dispatcher) {
        URL(
            BuildConfig.BASE_URL +
                    ServiceMethod.GET_SEARCH.method +
                    defaultQuery +
                    "&per_page=$PER_PAGE" +
                    "&page=$page"
        )
            .createHttpUrlConnection()
            ?.run {
                requestMethod = RequestMethod.GET.name

                println("response code is... $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return@withContext format.decodeFromStream<FlickrResponse>(inputStream).photos
                }
                disconnect()
            }
        throw IOException("Failed call network")
    }

    private fun URL.createHttpUrlConnection(): HttpsURLConnection? {
        return (openConnection() as? HttpsURLConnection)?.apply {
            doInput = true
            doOutput = true
            connectTimeout = NETWORK_TIMEOUT
        }
    }


}