package com.shishiapp.interview.di

import android.content.Context
import androidx.room.Room
import com.interview.musicsearch.data.ContentDao
import com.interview.musicsearch.data.ContentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val DATABASE_NAME = "music-db"

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): ContentDatabase {

        return Room.databaseBuilder(context, ContentDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideContentDao(database: ContentDatabase): ContentDao {
        return database.contentDao()
    }

}