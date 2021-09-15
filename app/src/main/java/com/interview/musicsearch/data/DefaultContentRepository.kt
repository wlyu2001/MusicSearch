package com.interview.musicsearch.data

import com.interview.musicsearch.api.ContentAPI
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.data.model.Track

class DefaultContentRepository(
    private val contentService: ContentAPI,
    private val dao: ContentDao
) : ContentRepository {

    override suspend fun searchArtists(queryText: String): List<Artist> {
        return if (queryText.isEmpty()) {
            listOf()
        } else {
            contentService.searchArtists(queryText).data
        }
    }

    override suspend fun fetchArtistAlbums(id: String): List<Album> {
        return contentService.fetchArtistAlbums(id).data
    }

    override suspend fun fetchArtist(id: String): Artist {
        return contentService.fetchArtist(id)
    }

    override suspend fun fetchAlbum(id: String): Album {
        return contentService.fetchAlbum(id)
    }

    override suspend fun fetchAlbumTracks(id: String): List<Track> {
        return contentService.fetchAlbumTracks(id).data
    }
}