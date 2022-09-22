package com.example.work

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.domain.combine.toScheduleEntity
import com.example.server.schedule.FakeRemoteScheduleDataSource
import com.example.server.schedule.RemoteScheduleRepository
import com.practice.database.schedule.FakeScheduleDataSource
import com.practice.database.schedule.ScheduleRepository
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FetchRemoteScheduleWorkerTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val localRepository = ScheduleRepository(FakeScheduleDataSource())
    private val remoteRepository = RemoteScheduleRepository(FakeRemoteScheduleDataSource())

    @Test
    fun doWork(): Unit = runBlocking {
        val months = (1..12)
        val remoteSchedules = months.map { month ->
            remoteRepository.getSchedules(2022, month).schedules
        }.flatten().map { it.toScheduleEntity() }

        val worker = buildWorker()
        val result = worker.doWork()
        assertThat(result).isEqualTo(ListenableWorker.Result.success())

        val allSchedules = months.map { month ->
            localRepository.getSchedules(2022, month)
        }.flatten()
        assertThat(allSchedules).isNotEmpty
            .containsAll(remoteSchedules)
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
                        remoteRepository
                    )
                }
            }).build()
}