package com.practice.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hsk.ktx.date.Date
import com.practice.domain.meal.Meal
import com.practice.notification.message.MealMessageBuilder
import com.practice.notification.message.ScheduleMessageBuilder

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
        val groupName = context.getString(R.string.daily_alarm_channel_group)
        notificationManager.createNotificationChannelGroup(
            NotificationChannelGroup(
                dailyNoticeChannelGroupId,
                groupName,
            )
        )
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
        notificationManager: NotificationManager,
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
        val channel = NotificationChannel(id, name, importance).apply {
            description = descriptionText
            if (groupId.isNotEmpty()) {
                group = groupId
            }
        }
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * 실제 알림을 만드는 코드
     */
    fun createMealNotification(context: Context, date: Date, meals: List<Meal>): Int {
        val mealMessageBuilder = MealMessageBuilder(date, meals)
        val title = mealMessageBuilder.title
        val body = mealMessageBuilder.body

        val builder = buildMealNotification(context, title, body)

        val notificationId: Int = date.toEpochSecond().toInt()
        notifyIfPermissionIsGranted(context, builder, notificationId, "meal")

        return notificationId
    }

    fun createScheduleNotification(context: Context, date: Date, contents: List<String>): Int {
        val scheduleMessageBuilder = ScheduleMessageBuilder(date, contents)
        val title = scheduleMessageBuilder.title
        val body = scheduleMessageBuilder.body

        val builder = buildScheduleNotification(context, title, body)

        val notificationId: Int = date.toEpochSecond().toInt() + 1
        notifyIfPermissionIsGranted(context, builder, notificationId, "schedule")

        return notificationId
    }

    private fun buildMealNotification(
        context: Context,
        title: String,
        body: String,
    ): NotificationCompat.Builder {
        return buildNotification(
            context = context,
            channelId = dailyNoticeMealChannelId,
            iconId = R.drawable.restaurant,
            title = title,
            body = body,
        )
    }

    private fun buildScheduleNotification(
        context: Context,
        title: String,
        body: String,
    ): NotificationCompat.Builder {
        return buildNotification(
            context = context,
            channelId = dailyNoticeScheduleChannelId,
            iconId = R.drawable.calendar,
            title = title,
            body = body,
        )
    }

    private fun buildNotification(
        context: Context,
        channelId: String,
        iconId: Int,
        title: String,
        body: String,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconId)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
    }

    private fun notifyIfPermissionIsGranted(
        context: Context,
        builder: NotificationCompat.Builder,
        notificationId: Int,
        logTitle: String = ""
    ) {
        with(NotificationManagerCompat.from(context)) {
            if ((ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED) && areNotificationsEnabled()
            ) {
                notify(notificationId, builder.build())
                log("notification $logTitle is sent!")
            } else {
                log("notification $logTitle could not be sent because of the permission lack")
            }
        }
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    private const val TAG = "BlinderNotificationManager"

    private const val dailyNoticeChannelGroupId = "daily-notice-channel-group"

    private const val dailyNoticeMealChannelId = "daily-notice-meal-channel"
    private const val dailyNoticeScheduleChannelId = "daily-notice-schedule-channel"

}