package tv.anilibria.module.data.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.module.data.restapi.datasource.remote.api.ScheduleRemoteDataSource
import tv.anilibria.module.domain.entity.schedule.ScheduleDay
import tv.anilibria.plugin.data.storage.ObservableData
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val scheduleApi: ScheduleRemoteDataSource,
    private val releaseUpdateHolder: ReleaseUpdateHelper
) {

    private val dataRelay = ObservableData<List<ScheduleDay>>()

    fun observeSchedule(): Flow<List<ScheduleDay>> {
        return dataRelay.observe().filterNotNull()
    }

    suspend fun loadSchedule(): List<ScheduleDay> {
        return scheduleApi.getSchedule().also { schedule ->
            releaseUpdateHolder.update(schedule.map { it.items }.flatten())
            dataRelay.put(schedule)
        }
    }
}