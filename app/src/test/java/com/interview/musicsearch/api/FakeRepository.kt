package com.interview.musicsearch.api

import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.DataError
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.data.model.Track
import kotlinx.coroutines.CompletableDeferred

class FakeRepository : ContentRepository {
    private var completable = CompletableDeferred<List<Artist>>()

    fun sendCompletionToAllCurrentRequests() {
        completable.complete(listOf())
        completable = CompletableDeferred()
    }

    fun sendErrorToCurrentRequests() {
        completable.completeExceptionally(DataError("", Throwable()))
        completable = CompletableDeferred()
    }

    override suspend fun fetchArtist(id: String): Artist {
        TODO("Not yet implemented")
    }

    override suspend fun fetchArtistAlbums(id: String): List<Album> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAlbum(id: String): Album {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAlbumTracks(id: String): List<Track> {
        TODO("Not yet implemented")
    }

    override suspend fun searchArtists(queryText: String): List<Artist> = completable.await()


}