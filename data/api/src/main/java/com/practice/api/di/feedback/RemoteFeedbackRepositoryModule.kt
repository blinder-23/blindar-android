package com.practice.api.di.feedback

import com.practice.api.feedback.RemoteFeedbackDataStore
import com.practice.api.feedback.RemoteFeedbackRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteFeedbackRepositoryModule {
    @Singleton
    @Provides
    fun provideRemoteFeedbackRepository(
        dataStore: RemoteFeedbackDataStore
    ) = RemoteFeedbackRepository(dataStore)

}