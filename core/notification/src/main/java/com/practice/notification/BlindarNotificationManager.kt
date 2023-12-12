package com.practice.notification

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

object BlindarNotificationManager {

    fun createNotificationChannels(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannelGroups(context, notificationManager)
        createNotificationChannels(context, notificationManager)
    }

    private fun createNotificationChannelGroups(
        context: Context,
        notificationManager: NotificationManager,
    ) {
        createDailyNoticeChannelGroup(context, notificationManager)
    }

    private fun createDailyNoticeChannelGroup(
        context: Context,
        notificationManager: NotificationManager,
    ) {
        runOnlyOverOreo {
            val groupName = context.getString(R.string.daily_alarm_channel_group)
            notificationManager.createNotificationChannelGroup(
                NotificationChannelGroup(
                    dailyNoticeChannelGroupId,
                    groupName,
                )
            )
        }
    }

    private fun createNotificationChannels(
        context: Context,
        notificationManager: NotificationManager,
    ) {
        createDailyNoticeMealChannel(context, notificationManager)
        createDailyNoticeScheduleChannel(context, notificationManager)
    }

    private fun createDailyNoticeMealChannel(
        context: Context,
        notificationManager: NotificationManager
    ) {
        val name = context.getString(R.string.daily_alarm_meal_channel_name)
        val description = context.getString(R.string.daily_alarm_meal_channel_description)
        createNotificationChannel(
            id = dailyNoticeMealChannelId,
            name = name,
            descriptionText = description,
            importance = NotificationManager.IMPORTANCE_DEFAULT,
            groupId = dailyNoticeChannelGroupId,
            notificationManager = notificationManager,
        )
    }

    private fun createDailyNoticeScheduleChannel(
        context: Context,
        notificationManager: NotificationManager,
    ) {
        val name = context.getString(R.string.daily_alarm_schedule_channel_name)
        val description = context.getString(R.string.daily_alarm_schedule_channel_description)
        createNotificationChannel(
            id = dailyNoticeScheduleChannelId,
            name = name,
            descriptionText = description,
            importance = NotificationManager.IMPORTANCE_DEFAULT,
            groupId = dailyNoticeChannelGroupId,
            notificationManager = notificationManager,
        )
    }

    private fun createNotificationChannel(
        id: String,
        name: String,
        descriptionText: String,
        importance: Int,
        groupId: String = "",
        notificationManager: NotificationManager,
    ) {
        runOnlyOverOreo {
            val channel = NotificationChannel(id, name, importance).apply {
                description = descriptionText
                if (groupId.isNotEmpty()) {
                    group = groupId
                }
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    private fun runOnlyOverOreo(block: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            block()
        }
    }

    private const val dailyNoticeChannelGroupId = "daily-notice-channel-group"

    private const val dailyNoticeMealChannelId = "daily-notice-meal-channel"
    private const val dailyNoticeScheduleChannelId = "daily-notice-schedule-channel"

}