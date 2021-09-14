package com.shishiapp.interview.di

import com.interview.musicsearch.api.ContentAPI
import com.interview.musicsearch.data.ContentDao
import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.DefaultContentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideContentRepository(service: ContentAPI, dao: ContentDao): ContentRepository {
        return DefaultContentRepository(service, dao)
    }

}