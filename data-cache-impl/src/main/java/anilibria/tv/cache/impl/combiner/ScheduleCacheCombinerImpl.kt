package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.ScheduleCache
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.cache.combiner.ScheduleCacheCombiner
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import anilibria.tv.domain.entity.common.keys.ScheduleKey
import anilibria.tv.domain.entity.converter.ScheduleDayRelativeConverter
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.schedule.ScheduleDay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleCacheCombinerImpl(
    private val scheduleCache: ScheduleCache,
    private val releaseCache: ReleaseCacheCombiner,
    private val relativeConverter: ScheduleDayRelativeConverter
) : ScheduleCacheCombiner {

    override fun observeList(): Observable<List<ScheduleDay>> = scheduleCache
        .observeList()
        .switchMap { relativeItems ->
            releaseCache
                .observeSome(relativeItems.toReleaseKeys())
                .map(getSourceCombiner(relativeItems))
        }

    override fun getList(): Single<List<ScheduleDay>> = scheduleCache
        .getList()
        .flatMap { relativeItems ->
            releaseCache
                .getSome(relativeItems.toReleaseKeys())
                .map(getSourceCombiner(relativeItems))
        }

    override fun insert(items: List<ScheduleDay>): Completable = Completable.defer {
        val putRelease = releaseCache.insert(items.map { it.items }.flatten())
        val putSchedule = scheduleCache.insert(items.map { relativeConverter.toRelative(it) })
        Completable.concat(listOf(putRelease, putSchedule))
    }

    override fun remove(keys: List<ScheduleKey>): Completable = scheduleCache.remove(keys)

    override fun clear(): Completable = scheduleCache.clear()

    private fun getSourceCombiner(relativeItems: List<ScheduleDayRelative>) = Function<List<Release>, List<ScheduleDay>> { releaseItems ->
        relativeItems.map { relative ->
            relativeConverter.fromRelative(relative, releaseItems)
        }
    }

    private fun List<ScheduleDayRelative>.toReleaseKeys() = map { it.releaseIds }.flatten().map { ReleaseKey(it) }
}