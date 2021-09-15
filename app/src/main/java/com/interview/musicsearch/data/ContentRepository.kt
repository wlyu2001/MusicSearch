package com.interview.musicsearch.data

import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.data.model.Track

interface ContentRepository {

    suspend fun searchArtists(queryText: String): List<Artist>

    suspend fun fetchArtistAlbums(id: String): List<Album>

    suspend fun fetchArtist(id: String): Artist

    suspend fun fetchAlbum(id: String): Album

    suspend fun fetchAlbumTracks(id: String): List<Track>
}


class DataError(message: String, cause: Throwable?) : Throwable(message, cause)