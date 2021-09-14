package com.interview.musicsearch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album")
data class Album(
    @PrimaryKey val id: String,
    val title: String,
    val cover_small: String,
    val cover_medium: String,
    val cover_big: String
)