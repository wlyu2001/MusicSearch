package com.interview.musicsearch.data

import com.interview.musicsearch.api.ContentAPI
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.data.model.Track
import kotlinx.coroutines.withTimeout

private const val TIME_OUT = 5_000L

class DefaultContentRepository(
    private val contentService: ContentAPI,
    private val dao: ContentDao
) : ContentRepository {

    override suspend fun searchArtists(queryText: String): List<Artist> {
        return if (queryText.isEmpty()) {
            listOf()
        } else {
            try {
                withTimeout(TIME_OUT) {
                    contentService.searchArtists(queryText).data
                }
            } catch (cause: Throwable) {
                throw DataError("Unable to finish search", cause)
            }
        }
    }

    override suspend fun fetchArtistAlbums(id: String): List<Album> {
        return try {
            withTimeout(TIME_OUT) {
                contentService.fetchArtistAlbums(id).data
            }
        } catch (cause: Throwable) {
            throw DataError("Unable to fetch artist $id's albums", cause)
        }
    }

    override suspend fun fetchArtist(id: String): Artist {
        return try {
            withTimeout(TIME_OUT) {
                contentService.fetchArtist(id)
            }
        } catch (cause: Throwable) {
            throw DataError("Unable to fetch artist $id", cause)
        }
    }

    override suspend fun fetchAlbum(id: String): Album {
        return try {
            withTimeout(TIME_OUT) {
                contentService.fetchAlbum(id)
            }
        } catch (cause: Throwable) {
            throw DataError("Unable to fetch album $id", cause)
        }
    }

    override suspend fun fetchAlbumTracks(id: String): List<Track> {
        return try {
            withTimeout(TIME_OUT) {
                contentService.fetchAlbumTracks(id).data
            }
        } catch (cause: Throwable) {
            throw DataError("Unable to fetch album $id's tracks", cause)
        }
    }
}