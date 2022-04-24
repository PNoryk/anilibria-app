package tv.anilibria.feature.content.data.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.local.CacheUpdateStrategy
import tv.anilibria.feature.content.data.local.ReleaseCacheHelper
import tv.anilibria.feature.content.data.local.ReleaseUpdateHelper
import tv.anilibria.feature.content.data.remote.datasource.remote.api.ScheduleRemoteDataSource
import tv.anilibria.feature.content.types.schedule.ScheduleDay
import tv.anilibria.plugin.data.storage.InMemoryDataHolder
import tv.anilibria.plugin.data.storage.ObservableData

@InjectConstructor
class ScheduleRepository(
    private val scheduleApi: ScheduleRemoteDataSource,
    private val releaseUpdateHolder: ReleaseUpdateHelper,
    private val releaseCacheHelper: ReleaseCacheHelper
) {

    private val dataRelay = ObservableData<List<ScheduleDay>?>(InMemoryDataHolder())

    fun observeSchedule(): Flow<List<ScheduleDay>> {
        return dataRelay.observe().filterNotNull()
    }

    suspend fun loadSchedule(): List<ScheduleDay> {
        return scheduleApi.getSchedule().also { schedule ->
            val releases = schedule.map { it.items }.flatten()
            releaseCacheHelper.update(releases, CacheUpdateStrategy.MERGE)
            releaseUpdateHolder.update(releases)
            dataRelay.put(schedule)
        }
    }

}