package com.interview.musicsearch.util

import android.content.UriMatcher
import android.net.Uri


const val SEARCH_ARTIST = 1
const val ARTIST_ALBUMS = 2
const val ALBUM_TRACKS = 3

const val SCHEME = "myapp"
const val AUTHORITY = "com.shishiapp"


object UriUtil {

    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {

        addURI(AUTHORITY, "search/artists", SEARCH_ARTIST)
        addURI(AUTHORITY, "artist/#/albums", ARTIST_ALBUMS)
        addURI(AUTHORITY, "album/#/tracks", ALBUM_TRACKS)
    }

    fun getType(uri: Uri): Int {
        return sURIMatcher.match(uri)
    }

    fun getId(uri: Uri): String? {
        return when(sURIMatcher.match(uri)) {
            ARTIST_ALBUMS ->
                uri.pathSegments[1]
            ALBUM_TRACKS ->
                uri.pathSegments[1]
            else ->
                null
        }
    }

    fun getContentUri(type: Int, id: String = ""): Uri {
        return when(type) {
            SEARCH_ARTIST ->
                Uri.Builder().scheme(SCHEME).authority(AUTHORITY).path("search/artists").build()
            ARTIST_ALBUMS ->
                Uri.Builder().scheme(SCHEME).authority(AUTHORITY).path("artist/$id/albums").build()
            ALBUM_TRACKS ->
                Uri.Builder().scheme(SCHEME).authority(AUTHORITY).path("album/$id/tracks").build()
            else ->
                throw Exception("Invalid content type")
        }
    }

    fun getAlbumTracksUri(id: String): Uri {
        return Uri.Builder().scheme(SCHEME).path("artist/$id/albums").build()
    }

    fun getArtistAlbumsUri(id: String): Uri {
        return Uri.Builder().scheme(SCHEME).path("album/$id/tracks").build()
    }

}