package com.interview.musicsearch.api

import androidx.lifecycle.LiveData
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.data.model.Data
import com.interview.musicsearch.data.model.Track
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentAPI {

    @GET("https://api.deezer.com/artist/{id}/albums")
    suspend fun fetchAlbumsOfArtist(@Path("id") id: String): Data<Album>

    @GET("https://api.deezer.com/album/{id}/tracks")
    suspend fun fetchTracksOfAlbum(@Path("id") id: String): Data<Track>

    @GET("https://api.deezer.com/search/artist")
    suspend fun searchArtists(@Query("q") text: String): Data<Artist>


}