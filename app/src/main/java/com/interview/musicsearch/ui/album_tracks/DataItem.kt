package com.interview.musicsearch.ui.album_tracks

import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Track

sealed class DataItem {
    data class TrackerItem(val track: Track) : DataItem() {
        override val id = track.id
    }

    data class DiskItem(val text: String) : DataItem() {
        override val id = text
    }

    data class AlbumItem(val album: Album) : DataItem() {
        override val id = "Album"
    }

    abstract val id: String
}