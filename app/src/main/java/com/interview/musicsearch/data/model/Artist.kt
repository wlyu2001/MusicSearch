package com.interview.musicsearch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist")
data class Artist(
    @PrimaryKey val id: String,
    val name: String,
    val picture_small: String,
    val picture_medium: String,
    val picture_big: String
)