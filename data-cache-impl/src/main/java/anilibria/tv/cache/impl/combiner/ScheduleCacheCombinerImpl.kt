package anilibria.tv.cache.impl.combiner

import anilibria.tv.cache.ScheduleCache
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.cache.combiner.ScheduleCacheCombiner
import anilibria.tv.domain.entity.converter.ScheduleDayRelativeConverter
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.schedule.ScheduleDay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
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
                .observeList(relativeItems.map { it.releaseIds }.flatten())
                .map(getSourceCombiner(relativeItems))
        }

    override fun getList(): Single<List<ScheduleDay>> = scheduleCache
        .getList()
        .flatMap { relativeItems ->
            releaseCache
                .getList(relativeItems.map { it.releaseIds }.flatten())
                .map(getSourceCombiner(relativeItems))
        }

    override fun putList(items: List<ScheduleDay>): Completable {
        val putRelease = releaseCache.putList(items.map { it.items }.flatten())
        val putSchedule = scheduleCache.putList(items.map { relativeConverter.toRelative(it) })
        return Completable.concat(listOf(putRelease, putSchedule))
    }

    override fun removeList(items: List<ScheduleDay>): Completable = scheduleCache
        .removeList(items.map { relativeConverter.toRelative(it) })

    override fun clear(): Completable = scheduleCache.clear()

    private fun getSourceCombiner(relativeItems: List<ScheduleDayRelative>) = Function<List<Release>, List<ScheduleDay>> { releaseItems ->
        relativeItems.map { relative ->
            relativeConverter.fromRelative(relative, releaseItems)
        }
    }
}