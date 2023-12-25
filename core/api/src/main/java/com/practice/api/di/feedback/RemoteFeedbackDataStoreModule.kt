package com.practice.api.di.feedback

import com.practice.api.feedback.RemoteFeedbackDataStore
import com.practice.api.feedback.RemoteFeedbackDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteFeedbackDataStoreModule {
    @Binds
    abstract fun bindRemoteFeedbackDataStore(
        impl: RemoteFeedbackDataStoreImpl
    ): RemoteFeedbackDataStore
}