package com.interview.musicsearch.api

import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.data.model.Data
import com.interview.musicsearch.data.model.Track
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentAPI {

    @GET("https://api.deezer.com/artist/{id}")
    suspend fun fetchArtist(@Path("id") id: String): Artist

    @GET("https://api.deezer.com/artist/{id}/albums")
    suspend fun fetchArtistAlbums(@Path("id") id: String): Data<Album>

    @GET("https://api.deezer.com/album/{id}")
    suspend fun fetchAlbum(@Path("id") id: String): Album

    @GET("https://api.deezer.com/album/{id}/tracks")
    suspend fun fetchAlbumTracks(@Path("id") id: String): Data<Track>

    @GET("https://api.deezer.com/search/artist")
    suspend fun searchArtists(@Query("q") text: String): Data<Artist>


}