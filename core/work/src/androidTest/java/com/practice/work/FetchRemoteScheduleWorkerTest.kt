package com.practice.work

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.practice.api.schedule.FakeRemoteScheduleDataSource
import com.practice.api.schedule.RemoteScheduleRepository
import com.practice.preferences.FakePreferencesRepository
import com.practice.preferences.PreferencesRepository
import com.practice.schedule.FakeScheduleDataSource
import com.practice.schedule.ScheduleRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FetchRemoteScheduleWorkerTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val localRepository = ScheduleRepository(FakeScheduleDataSource())
    private val remoteRepository = RemoteScheduleRepository(FakeRemoteScheduleDataSource())
    private val preferencesRepository: PreferencesRepository = FakePreferencesRepository()

    @Test
    fun doWork(): Unit = runBlocking {
        val schoolCode = 3
        preferencesRepository.updateSelectedSchool(schoolCode, "name")

        val months = (1..12)
        val remoteSchedules = months.map { month ->
            remoteRepository.getSchedules(schoolCode, 2022, month).schedules
        }.flatten()

        val worker = buildWorker()
        val result = worker.doWork()
        assertThat(result).isEqualTo(ListenableWorker.Result.success())

        val allSchedules = months.map { month ->
            localRepository.getSchedules(schoolCode, 2022, month).first()
        }.flatten()
        assertThat(allSchedules).isNotEmpty
            .containsExactlyInAnyOrderElementsOf(remoteSchedules)
    }

    private fun buildWorker(): FetchRemoteScheduleWorker =
        TestListenableWorkerBuilder<FetchRemoteScheduleWorker>(context)
            .setWorkerFactory(object : WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker {
                    return FetchRemoteScheduleWorker(
                        context,
                        workerParameters,
                        localRepository,
                        remoteRepository,
                        preferencesRepository,
                    )
                }
            }).build()
}