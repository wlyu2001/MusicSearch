package com.interview.musicsearch.data

import com.interview.musicsearch.data.model.Artist

interface ContentRepository {

    suspend fun searchArtists(text: String): List<Artist>

}


class DataError(message: String, cause: Throwable?) : Throwable(message, cause)