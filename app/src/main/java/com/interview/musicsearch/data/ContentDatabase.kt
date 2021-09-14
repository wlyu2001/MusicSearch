package com.interview.musicsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.data.model.Track

@Database(
    entities = [Album::class, Artist::class, Track::class],
    version = 1
)

abstract class ContentDatabase : RoomDatabase() {

    abstract fun contentDao(): ContentDao
}