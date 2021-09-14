package com.interview.musicsearch.data

import com.interview.musicsearch.api.ContentAPI
import com.interview.musicsearch.data.model.Artist

class DefaultContentRepository(
    private val contentService: ContentAPI,
    private val dao: ContentDao
) : ContentRepository {

    override suspend fun searchArtists(text: String): List<Artist> {
        return if (text.isEmpty()) {
            listOf()
        } else {
            contentService.searchArtists(text).data
        }
    }
}