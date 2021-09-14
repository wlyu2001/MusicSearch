package com.interview.musicsearch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track")
data class Track(
    @PrimaryKey val id: String,
    val title: String,
    val title_short: String,

    val disk_numer: Int,
)