package com.interview.musicsearch.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "track")
data class Track(
    @PrimaryKey val id: String,
    val title: String,
    val track_position: Int,
    val disk_number: Int,

    ) {
    @Ignore
    var artist: SimpleArtist? = null
}