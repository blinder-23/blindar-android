package com.practice.api.di.feedback

import com.practice.api.feedback.FeedbackApi
import com.practice.api.feedback.feedbackApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeedbackApiModule {
    @Singleton
    @Provides
    fun provideFeedbackApi(): FeedbackApi = feedbackApi
}