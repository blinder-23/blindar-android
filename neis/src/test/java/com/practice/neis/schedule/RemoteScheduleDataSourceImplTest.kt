package com.practice.neis.schedule

import com.practice.neis.common.NeisApi
import com.practice.neis.schedule.retrofit.scheduleApi
import com.practice.neis.schedule.util.ScheduleDeserializerException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteScheduleDataSourceImplTest {

    private val source: RemoteScheduleDataSource = RemoteScheduleDataSourceImpl(NeisApi.scheduleApi)

    @Test
    fun getSchedules() = runTest {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1

        val result = source.getSchedules(currentYear, currentMonth)
        assert(result.isNotEmpty())
    }

    @Test
    fun getSchedules_NoData(): Unit = runTest {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, 1)
        }
        val year = calendar.get(Calendar.YEAR) + 1
        val month = calendar.get(Calendar.MONTH)

        assertThrows<ScheduleDeserializerException>("해당하는 데이터가 없습니다.") {
            source.getSchedules(year, month)
        }
    }

}